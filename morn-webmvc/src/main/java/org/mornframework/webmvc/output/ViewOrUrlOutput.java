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

import javax.servlet.ServletException;

import org.mornframework.webmvc.context.ContextCommons;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class ViewOrUrlOutput extends Output{

	public ViewOrUrlOutput(String view){
		this.view = view;
	}
	
	@Override
	public void output() {
		try {
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
