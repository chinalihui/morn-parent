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
package org.mornframework.webmvc.output;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mornframework.webmvc.context.ContextCommons;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public abstract class Output {
	
	protected String view;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public static String getEncoding() {
		return ContextCommons.DEFAULT_ENCODING;
	}
	
	public Output setOutput(HttpServletRequest request,
			HttpServletResponse response){
		this.request = request;
		this.response = response;
		return this;
	}

	public abstract void output();
}
