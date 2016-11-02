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
package org.mornframework.webmvc.support;

import java.lang.annotation.Annotation;
import java.util.List;

import org.mornframework.webmvc.interceptor.ActionInterceptor;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class ReqMapping {

	private Class<?>[] paramsTypes;
	private Class<?> returnType;
	private String reqUri;
	private String methodName;
	private String[] paramNames;
	private Annotation[] annotations;
	private Annotation[][] paramsAnnotation;
	private String actionName;
	private List<ActionInterceptor> interceptors;
	private RequestMethod[] requestMethod;
	
	public ReqMapping(Class<?>[] paramsTypes,
			Annotation[][] paramsAnnotation,Class<?> returnType,
			String reqUri,String methodName,String[] paramNames,
			Annotation[] annotations,String actionName,
			List<ActionInterceptor> interceptors,
			RequestMethod[] requestMethod){
		this.paramsTypes = paramsTypes;
		this.paramsAnnotation = paramsAnnotation;
		this.returnType = returnType;
		this.reqUri = reqUri;
		this.methodName = methodName;
		this.paramNames = paramNames;
		this.annotations = annotations;
		this.actionName = actionName;
		this.interceptors = interceptors;
		this.requestMethod = requestMethod;
	}
	
	public RequestMethod[] getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod[] requestMethod) {
		this.requestMethod = requestMethod;
	}

	public Annotation[][] getParamsAnnotation() {
		return paramsAnnotation;
	}

	public void setParamsAnnotation(Annotation[][] paramsAnnotation) {
		this.paramsAnnotation = paramsAnnotation;
	}

	public List<ActionInterceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<ActionInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}
	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getReqUri() {
		return reqUri;
	}

	public void setReqUri(String reqUri) {
		this.reqUri = reqUri;
	}

	public Class<?>[] getParamsTypes() {
		return paramsTypes;
	}
	public void setParamsTypes(Class<?>[] paramsTypes) {
		this.paramsTypes = paramsTypes;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	
	public boolean stringReturnType(){
		return returnType == String.class;
	}
	
	public boolean exsitAnnotation(Class<?> clazz){
		if(annotations != null){
			for (Annotation annotation : annotations) {
				if(annotation.annotationType() == clazz){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isRequestMethod(String method){
		if(requestMethod == null || requestMethod.length == 0){
			return true;
		}
		for(RequestMethod reqm : requestMethod){
			if(method.equals(reqm.toString())){
				return true;
			}
		}
		return false;
	}
	
	public Annotation getParamAnnotation(int paramIndex,Class<?> clazz){
		if(paramsAnnotation == null){
			return null;
		}
		for(Annotation ann : paramsAnnotation[paramIndex]){
			if(ann.annotationType() == clazz){
				return ann;
			}
		}
		return null;
	}
}
