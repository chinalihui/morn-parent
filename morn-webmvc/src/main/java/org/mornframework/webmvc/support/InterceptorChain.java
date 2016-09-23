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

import org.mornframework.webmvc.interceptor.ActionInterceptor;

/**
 * @author Jeff.Li
 * @date 2016年9月22日
 */
public class InterceptorChain implements Comparable<InterceptorChain>{

	private int order;
	private String[] path;
	private ActionInterceptor interceptor;

	public InterceptorChain(int order, String[] path,
			ActionInterceptor interceptor) {
		super();
		this.order = order;
		this.path = path;
		this.interceptor = interceptor;
	}
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String[] getPath() {
		return path;
	}
	public void setPath(String[] path) {
		this.path = path;
	}
	public ActionInterceptor getInterceptor() {
		return interceptor;
	}
	public void setInterceptor(ActionInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	public int compareTo(InterceptorChain o) {
		return this.getOrder() - o.getOrder();
	}
}
