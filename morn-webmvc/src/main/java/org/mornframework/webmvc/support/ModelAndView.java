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

package org.mornframework.webmvc.support;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class ModelAndView {

	private String viewName;
	private Map<String,Object> model;
	
	public ModelAndView(String viewName){
		this.viewName = viewName;
	}
	
	public ModelAndView(String viewName,Map<String, Object> model){
		this.viewName = viewName;
		this.model = model;
	}
	
	public ModelAndView addObject(String attributeName,Object attributeValue){
		if(model == null){
			model = new LinkedHashMap<String, Object>();
		}
		model.put(attributeName, attributeValue);
		return this;
	}
	
	public ModelAndView addMapObject(Map<String,Object> attributes){
		if(model == null){
			model = new LinkedHashMap<String, Object>();
		}
		model.putAll(attributes);
		return this;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

}
