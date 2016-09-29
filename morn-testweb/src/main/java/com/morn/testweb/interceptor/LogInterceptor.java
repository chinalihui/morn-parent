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

package com.morn.testweb.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mornframework.context.annotation.Interceptor;
import org.mornframework.webmvc.interceptor.ActionInterceptor;

@Interceptor(order=0,path="/**")
public class LogInterceptor implements ActionInterceptor{

	public boolean beforeAction(HttpServletRequest request,
			HttpServletResponse response, Object action) {
		return true;
	}

	public void afterAction(HttpServletRequest request,
			HttpServletResponse response, Object action, Object result) {
	}

	public void completionAction(HttpServletRequest request,
			HttpServletResponse response, Object action, Exception e) {
	}

}
