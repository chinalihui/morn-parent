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
package org.mornframework.webmvc.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.servlet.FilterConfig;

import org.mornframework.context.annotation.Action;
import org.mornframework.context.annotation.Interceptor;
import org.mornframework.context.beans.factory.AbstractFactoryBean;
import org.mornframework.context.beans.factory.ContextFactoryBean;
import org.mornframework.core.util.StringUtils;
import org.mornframework.webmvc.annotation.RequestRoute;
import org.mornframework.webmvc.handler.ActionHandler;
import org.mornframework.webmvc.handler.Handler;
import org.mornframework.webmvc.interceptor.ActionInterceptor;
import org.mornframework.webmvc.support.InterceptorChain;
import org.mornframework.webmvc.support.ReqMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class MornWebApplicationContext implements MornApplication{

	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	private Map<String,ReqMapping> reqMappingMaps;
	private FilterConfig filterConfig;
	private List<InterceptorChain> interceptorChains;
	private String scanPackage;
	private Set<Class<?>> classList;
	private Handler handler;
	private AbstractFactoryBean factoryBean;
	
	public MornWebApplicationContext(){
		reqMappingMaps = new LinkedHashMap<String, ReqMapping>();
	}
	
	public Handler getHandler(){
		return handler;
	}
	
	public void init(String scanPackage,FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
		this.scanPackage = scanPackage;
		
		initFactoryBean();
		initInterceptor();
		initWebAction();
		handler = new ActionHandler(factoryBean,reqMappingMaps);
	}
	
	private void initWebAction(){
		ClassPool pool = ClassPool.getDefault();
		for (Class<?> cl : classList) {
			Action action = cl.getAnnotation(Action.class);
			if(action == null) continue;
			
			String actionName = factoryBean.getBeanName(cl);
			if(LOG.isDebugEnabled()){
				LOG.debug("Morn Framework put action:" + actionName
						+ ",actionClass:" + cl);
			}
			
			CtClass cc = null;
			pool.insertClassPath(new ClassClassPath(cl));
			try {
				cc = pool.get(cl.getName());
			} catch (NotFoundException e1) {
				e1.printStackTrace();
			}
			
			RequestRoute actionReq = cl.getAnnotation(RequestRoute.class);
			String actionUrl = "";
			if(actionReq != null && StringUtils.isNotEmpty(actionReq.value())){
				actionUrl = actionReq.value();
			}
			
			Method[] methods = cl.getMethods();
			for (Method method : methods) {
				RequestRoute req = method.getAnnotation(RequestRoute.class);
				if(req != null){
					String uri = actionUrl + req.value();
					try {
						CtMethod cm = cc.getDeclaredMethod(method.getName());
						MethodInfo methodInfo = cm.getMethodInfo();  
			            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
			            if (attr == null) continue;
			            
			            String[] paramNames = new String[cm.getParameterTypes().length];
			            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			            for (int i = 0; i < paramNames.length; i++)
			                paramNames[i] = attr.variableName(i + pos);
			            
			            reqMappingMaps.put(uri,new ReqMapping(method.getParameterTypes(),
			            									  method.getParameterAnnotations(),
														  	  method.getReturnType(),
														      req.value(),
														      method.getName(),
														      paramNames,
														      method.getAnnotations(),
														      actionName,
														      getUriInterceptor(uri),
														      req.method()
														      )
			            );
			            if(LOG.isInfoEnabled()){
			            	LOG.info("Application add action url:" + uri + ",mapping actionClass:" + cl);
			            }
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void initInterceptor(){
		interceptorChains = new ArrayList<InterceptorChain>();
		Map<Integer,String> orderMap = new HashMap<Integer, String>();
		for (Class<?> cl : classList) {
			Interceptor interceptor = cl.getAnnotation(Interceptor.class);
			if(interceptor == null) continue;
			
			Class<?>[] interfaces = cl.getInterfaces();
			boolean isInterceptorInterface = false;
			for (Class<?> interfa : interfaces) {
				if(interfa == ActionInterceptor.class){
					isInterceptorInterface = true;
					break;
				}
			}
			
			if(!isInterceptorInterface){
				throw new RuntimeException("Interceptor class:" + cl 
						+ " not implements:" + ActionInterceptor.class);
			}
			
			int order = interceptor.order();
			if(orderMap.containsKey(order)){
				throw new RuntimeException("Interceptor " + cl 
						+ " order:" + order +" exist ");
			}
			
			orderMap.put(order, "");
			String[] path = interceptor.path();
			if(path == null || path.length == 0){
				throw new NullPointerException("Interceptor " + cl 
						+ " @Interceptor path not set. ");
			}
			
			for(String p : path){
				if(StringUtils.isEmpty(p) || p.indexOf("/**") == -1){
					throw new NullPointerException("Interceptor " + cl 
							+ " @Interceptor path is empty or path must include /**");
				}
			}
			
			String name = factoryBean.getBeanName(cl);
			ActionInterceptor actionInterceptor = (ActionInterceptor) factoryBean.getBean(name);
			interceptorChains.add(new InterceptorChain(order,path, actionInterceptor));
			LOG.info("Init Web Application Interceptor -->"+cl);
		}
		Collections.sort(interceptorChains);
	}
	
	public void initFactoryBean(){
		LOG.info("init Web Application Context ");
		factoryBean = new ContextFactoryBean(scanPackage,filterConfig.getInitParameter("contextProperties"));
		factoryBean.createContextBeans();
		classList = factoryBean.getAnnotationClasses();
	}
	
	public List<ActionInterceptor> getUriInterceptor(String uri){
		if(interceptorChains.isEmpty()){
			return null;
		}
		
		List<ActionInterceptor> interceptorList = new ArrayList<ActionInterceptor>();
		boolean uriExistsInterceptor = false;
		for (InterceptorChain chain : interceptorChains) {
			String[] paths = chain.getPath();
			for(String path : paths){
				path = path.replace("**", "");
				if(uri.startsWith(path)){
					uriExistsInterceptor = true;
					interceptorList.add(chain.getInterceptor());
				}
			}
		}
		
		if(!uriExistsInterceptor){
			return null;
		}
		return interceptorList;
	}
	
	public void shutdown() {
		filterConfig = null;
		reqMappingMaps.clear();
		handler = null;
	}

}
