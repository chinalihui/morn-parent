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

package org.mornframework.webmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jeff.Li
 * @date 2016年9月22日
 */
public interface ActionInterceptor {

	/**
	 * action before execute
	 * @param request
	 * @param response
	 * @param action
	 * @return
	 */
	boolean beforeAction(HttpServletRequest request,
			HttpServletResponse response,Object action)throws Exception;
	
	/**
	 * action after execute
	 * @param request
	 * @param response
	 * @param action
	 * @param result
	 */
	void afterAction(HttpServletRequest request,
			HttpServletResponse response,Object action,Object result)throws Exception;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param action
	 * @param e
	 */
	void completionAction(HttpServletRequest request,
			HttpServletResponse response,Object action,Exception e);
	
}
