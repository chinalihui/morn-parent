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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mornframework.webmvc.support.ReqMapping;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public abstract class Handler {

	/**
	 * 
	 * @param uri
	 * @param request
	 * @param response
	 * @param flag
	 * @return
	 */
	public abstract Object handle(String uri, HttpServletRequest request,
			HttpServletResponse response,boolean[] flag);
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public abstract ReqMapping getReqMapping(String uri);
	
	/**
	 * 
	 * @param actionName
	 * @return
	 */
	public abstract Object getAction(String actionName);
	
}
