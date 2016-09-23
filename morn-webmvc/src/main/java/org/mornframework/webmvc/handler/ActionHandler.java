/**
* Copyright 2016-2026 the original author or authors.
*
* Welcome hot like Java Person,Contribution code for org.
* This framework Has been published on Git
*     https://github.com/chinalihui/morn-parent,
*     https://git.oschina.net/osjeff/morn-parent
* 
* If there are problems or ideas can be submitted to the
*     https://github.com/chinalihui/morn-parent/issues
*
*                                    Thanks.
*/
package org.mornframework.webmvc.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mornframework.webmvc.annotation.Json;
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
 */
public class ActionHandler extends Handler{
	
	private Map<String, Object> actionMap;
	private Map<String,ReqMapping> reqMappingMaps;
	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	
	public ActionHandler(Map<String, Object> actionMap,
			Map<String,ReqMapping> reqMappingMaps){
		this.actionMap = actionMap;
		this.reqMappingMaps = reqMappingMaps;
	}
	
	@Override
	public Object handle(String uri, HttpServletRequest request,
			HttpServletResponse response,boolean[] flag) {
		flag[0] = true;
		ReqMapping reqMapping = reqMappingMaps.get(uri);
		
		Object action = actionMap.get(reqMapping.getActionName());
		try {
			Class<?>[] paramsTypes = reqMapping.getParamsTypes();
			Method method = action.getClass().getMethod(
					reqMapping.getMethodName(),paramsTypes);
			try {
				Object result = null;
				Object[] args = bindParams(reqMapping,request,response);
				
				result = method.invoke(action,args);
				output(result,request,response,reqMapping);
				
				return result;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
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
		
		if(reqMapping.exsitAnnotation(Json.class)){
			new JsonOutput(result).setOutput(request, response).output();
			return;
		}
	}
	
	private Object[] bindParams(ReqMapping reqMapping,HttpServletRequest request,
			HttpServletResponse response){
		Class<?>[] paramsTypes = reqMapping.getParamsTypes();
		Object[] args = new Object[paramsTypes.length];
		String[] paramNames = reqMapping.getParamNames();
		
		for(int i = 0;i<paramNames.length;i++){
			Class<?> paramType = paramsTypes[i];
			if(paramType == HttpServletRequest.class)
				args[i] = request;
			else if(paramType == HttpServletResponse.class)
				args[i] = response;
			else{
				String value = request.getParameter(paramNames[i]);
				if(value == null){
					args[i] = defaultValue(paramType);
					continue;
				}
				if(paramType == String.class){
					args[i] = value;
				}else if(paramType == int.class){
					try{
						args[i] = Integer.parseInt(value);
					}catch(Exception e){
						args[i] = defaultValue(paramType);
					}
				}else if(paramType == boolean.class){
					try{
						args[i] = Boolean.parseBoolean(value);
					}catch(Exception e){
						args[i] = defaultValue(paramType);
					}
				}else if(paramType == double.class){
					try{
						args[i] = Double.parseDouble(value);
					}catch(Exception e){
						args[i] = defaultValue(paramType);
					}
				}
			}
		}
		return args;
	}
	
	public Object defaultValue(Class<?> type){
		if(type == int.class || type == byte.class || type == short.class){
			return 0;
		}else if(type == boolean.class){
			return false;
		}else if(type == long.class){
			return 0L;
		}else if(type == double.class){
			return 0.0d;
		}
		return null;
	}

	@Override
	public ReqMapping getReqMapping(String uri) {
		return reqMappingMaps.get(uri);
	}

	@Override
	public Object getAction(String actionName) {
		return actionMap.get(actionName);
	}

}
