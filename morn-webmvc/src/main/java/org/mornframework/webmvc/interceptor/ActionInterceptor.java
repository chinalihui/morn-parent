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
