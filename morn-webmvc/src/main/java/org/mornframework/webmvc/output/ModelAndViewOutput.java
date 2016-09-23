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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.mornframework.webmvc.context.ContextCommons;
import org.mornframework.webmvc.support.ModelAndView;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class ModelAndViewOutput extends Output{

	public ModelAndView modelAndView;
	
	public ModelAndViewOutput(Object modelAndView){
		this.modelAndView = (ModelAndView) modelAndView;
	}
	
	@Override
	public void output() {
		try {
			Map<String, Object> attributes = modelAndView.getModel();
			if(!attributes.isEmpty()){
				for(Map.Entry<String, Object> entry : attributes.entrySet()){
					request.setAttribute(entry.getKey(), entry.getValue());
				}
			}
			
			this.view = modelAndView.getViewName();
			if(view.startsWith("redirect:")){
				response.sendRedirect(view.substring(view.indexOf(":")+1));
			}else
				request.getRequestDispatcher(ContextCommons.WEB_PREFIX + view).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
