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
