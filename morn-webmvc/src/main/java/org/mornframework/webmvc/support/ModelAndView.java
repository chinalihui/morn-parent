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
