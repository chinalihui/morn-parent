/**
 * Copyright 2016- the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mornframework.webmvc.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mornframework.context.annotation.Value;
import org.mornframework.context.beans.factory.AbstractFactoryBean;
import org.mornframework.context.support.ApplicationProperties;
import org.mornframework.context.util.ClassUtil;
import org.mornframework.context.util.StringUtils;
import org.mornframework.webmvc.annotation.ResponseJson;
import org.mornframework.webmvc.exception.ServletRequestException;
import org.mornframework.webmvc.output.ErrorOutput;
import org.mornframework.webmvc.output.JsonOutput;
import org.mornframework.webmvc.output.ModelAndViewOutput;
import org.mornframework.webmvc.output.ViewOrUrlOutput;
import org.mornframework.webmvc.support.ModelAndView;
import org.mornframework.webmvc.support.ReqMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 * 1.处理前端请求
 */
public class ActionHandler extends Handler{
	
	private Map<String,ReqMapping> reqMappingMaps;
	protected AbstractFactoryBean factoryBean;
	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	protected ThreadLocal<Object> actionObject = new ThreadLocal<Object>();
	
	public ActionHandler(AbstractFactoryBean factoryBean,
			Map<String,ReqMapping> reqMappingMaps){
		this.factoryBean = factoryBean;
		this.reqMappingMaps = reqMappingMaps;
	}
	
	@Override
	public Object handle(String uri, HttpServletRequest request,
			HttpServletResponse response,boolean[] flag) throws Exception {
		flag[0] = true;
		ReqMapping reqMapping = reqMappingMaps.get(uri);
		
		try {
			boolean isRequestMethod = reqMapping.isRequestMethod(request.getMethod());
			if(!isRequestMethod)  throw new ServletRequestException("");
			
			Object action = getAction(reqMapping.getActionName());
			Class<?>[] paramsTypes = reqMapping.getParamsTypes();
			Method method = action.getClass().getMethod(reqMapping.getMethodName(),paramsTypes);
			Object result = null;
			Object[] args = bindParams(reqMapping,request,response);
			
			result = method.invoke(action,args);
			output(result,request,response,reqMapping);
			
			return result;
		} catch (ServletRequestException se){
			LOG.debug("Please check URL:"+uri+", request method [" + request.getMethod() + "]");
			new ErrorOutput(405, uri).setOutput(request, response).output();
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			new ErrorOutput(500, uri ,e).setOutput(request, response).output();
			throw e;
		}
		finally{
			actionObject.remove();
		}
	}
	
	public void output(Object result,HttpServletRequest request,
			HttpServletResponse response,ReqMapping reqMapping){
		if(result == null || reqMapping.getReturnType() == void.class){
			return;
		}
		
		if(reqMapping.stringReturnType()){
			new ViewOrUrlOutput(String.valueOf(result)).setOutput(request, response).output();
			return;
		}
		
		if(result instanceof ModelAndView){
			new ModelAndViewOutput(result).setOutput(request, response).output();
			return;
		}
		
		if(reqMapping.exsitAnnotation(ResponseJson.class)){
			new JsonOutput(result).setOutput(request, response).output();
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object[] bindParams(ReqMapping reqMapping,HttpServletRequest request,
			HttpServletResponse response){
		Class<?>[] paramsTypes = reqMapping.getParamsTypes();
		Object[] args = new Object[paramsTypes.length];
		String[] paramNames = reqMapping.getParamNames();
		
		/**
		 * 获取含有实体类属性的参数
		 */
		Map<String, Map<String, Object>> paramValue = new HashMap<String, Map<String, Object>>();
		Set<String> paramSet = request.getParameterMap().keySet();
		if(paramSet != null){
			for (String paramName : paramSet) {
				if(StringUtils.isNotEmpty(paramName)){
					int index = paramName.indexOf(".");
					if(index > 0){
						String prefix = paramName.substring(0,index);
						Map<String, Object> paramHash = paramValue.get(prefix);
						if(paramHash == null ){
							paramValue.put(prefix,new HashMap<String, Object>());
						}
						paramValue.get(prefix).put(paramName.substring(index+1,paramName.length())
								, request.getParameter(paramName));
					}
				}
			}
		}
		
		/**
		 * 循环设置方法类的请求参数
		 */
		for(int i = 0;i<paramNames.length;i++){
			Class<?> paramType = paramsTypes[i];
			Value propertiesValue = null;
			if(paramType == HttpServletRequest.class)
				args[i] = request;
			else if(paramType == HttpServletResponse.class)
				args[i] = response;
			else if(paramType == HttpSession.class)
				args[i] = request.getSession();
			else if(!ClassUtil.isJavaClass(paramType) && paramValue.containsKey(paramNames[i]))
				args[i] = pushEntityFields(paramValue.get(paramNames[i]),paramType);
			else if(ClassUtil.isJavaClass(paramType) && (propertiesValue = (Value)reqMapping.getParamAnnotation(i,Value.class)) != null && StringUtils.includeProperty(propertiesValue.value())){
				String strValue = StringUtils.propertyKey(propertiesValue.value());
				args[i] = ClassUtil.parseValue(ApplicationProperties.properties.get(strValue), paramType);
			}
			else{
				String value = request.getParameter(paramNames[i]);
				if(value == null){
					args[i] = ClassUtil.defaultValue(paramType);
					continue;
				}
				
				args[i] = ClassUtil.parseValue(value, paramType);
			}
		}
		return args;
	}
	
	public Object pushEntityFields(Map<String, Object> nameValues,Class<?> clazz){
		Object object = null;
		try {
			object = clazz.newInstance();
			for(Map.Entry<String, Object> nameValue : nameValues.entrySet()){
				String fieldName = nameValue.getKey();
				Method method = clazz.getMethod("set"+StringUtils.firstToUpperCase(fieldName)
						,clazz.getDeclaredField(fieldName).getType());
				method.invoke(object, nameValue.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	@Override
	public ReqMapping getReqMapping(String uri) {
		return reqMappingMaps.get(uri);
	}

	@Override
	public Object getAction(String actionName) {
		Object object = actionObject.get();
		if(object != null){
			return object;
		}
		object = factoryBean.getBean(actionName);
		if(object != null){
			actionObject.set(object);
			return object;
		}
		return object;
	}

}
