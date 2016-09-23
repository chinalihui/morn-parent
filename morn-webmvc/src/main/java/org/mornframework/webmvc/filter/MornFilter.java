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
package org.mornframework.webmvc.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mornframework.webmvc.context.ContextCommons;
import org.mornframework.webmvc.context.MornApplication;
import org.mornframework.webmvc.handler.Handler;
import org.mornframework.webmvc.interceptor.ActionInterceptor;
import org.mornframework.webmvc.support.ReqMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class MornFilter implements Filter{
	
	private MornApplication mornApplication;
	private int contextPathLength;
	private Handler handler;
	private String encoding = "UTF-8";
	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	
	public void init(FilterConfig filterConfig) throws ServletException {
		initMornApplication(filterConfig.getInitParameter("applicationInitClass"));
		
		mornApplication.init(filterConfig.getInitParameter("scanPackage")
				,filterConfig.getServletContext());
		handler = mornApplication.getHandler();
		
		String contextPath = filterConfig.getServletContext().getContextPath();
		contextPathLength = (contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length());
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		req.setCharacterEncoding(encoding);
		resp.setCharacterEncoding(encoding);
		
		String uri = req.getRequestURI();
		if(contextPathLength != 0){
			uri = uri.substring(contextPathLength);
		}
		
		boolean[] flag = {false};
		ReqMapping reqMapping = handler.getReqMapping(uri);
		if(uri.indexOf(".") == -1 && !"/".equals(uri) && reqMapping != null){
			LOG.info("request Application URL[" + uri + "]");
			Object actionObject = handler.getAction(reqMapping.getActionName());
			List<ActionInterceptor> interceptors = reqMapping.getInterceptors();
			int interceptorIndex = 0;
			
			try{
				/**
				 * 调用拦截器前置方法
				 */
				if(interceptors != null){
					for (int i = 0; i<interceptors.size();i++) {
						if(!interceptors.get(i).beforeAction(req, resp, actionObject)){
							triggerCompletionAction(interceptors,interceptorIndex,req, resp, actionObject, null);
							return;
						}
						interceptorIndex = i;
					}
				}
			
				/**
				 * 执行请求处理
				 */
				Object result = handler.handle(uri, req, resp, flag);
				
				/**
				 * 调用拦截器后置方法
				 */
				if(interceptors != null){
					for(int i = interceptors.size() - 1;i >= 0;i--){
						interceptors.get(i).afterAction(req, resp, actionObject, result);
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			triggerCompletionAction(interceptors,interceptorIndex,req, resp, actionObject, null);
		}
		
		if(!flag[0]){
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
		mornApplication.shutdown();
	}
	
	public void triggerCompletionAction(List<ActionInterceptor> interceptors,
			int interceptorIdx,
			HttpServletRequest request,
			HttpServletResponse response,
			Object action,
			Exception e){
		if(interceptors != null){
			for(int i = interceptorIdx;i >=0; i--){
				try{
					interceptors.get(i).completionAction(request, response, action, e);
				}
				catch(Throwable ex){
					LOG.error("execute Interceptor.completionAction method throws exception ",ex);
				}
			}
		}
	}
	
	private void initMornApplication(String applicationInitClass){
		if(applicationInitClass == null){
			applicationInitClass = ContextCommons.DEFAULT_APPLICATION_INIT_CLASS;
		}
		
		try{
			mornApplication = (MornApplication) Class.forName(applicationInitClass).newInstance();
		}catch(Exception e){
			throw new RuntimeException("instance Class:"+ applicationInitClass + " exception,", e);
		}
	}

}
