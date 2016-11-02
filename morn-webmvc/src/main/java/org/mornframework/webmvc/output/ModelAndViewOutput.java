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
