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
package org.mornframework.webmvc.context;

import javax.servlet.FilterConfig;

import org.mornframework.webmvc.handler.Handler;


/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public interface MornApplication {
	
	/**
	 * init application context
	 * @param scanPackage
	 * @param servletContext
	 */
	public void init(String scanPackage,
			FilterConfig filterConfig);
	
	/**
	 * stop server
	 */
	public void shutdown();
	
	/**
	 * get reqMapping handler
	 * @return
	 */
	public Handler getHandler();

}
