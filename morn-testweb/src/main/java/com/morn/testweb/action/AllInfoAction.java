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

package com.morn.testweb.action;

import java.util.HashMap;
import java.util.Map;

import org.mornframework.context.annotation.Action;
import org.mornframework.context.annotation.Inject;
import org.mornframework.context.annotation.Scope;
import org.mornframework.webmvc.annotation.RequestRoute;
import org.mornframework.webmvc.annotation.ResponseJson;

import com.morn.testweb.service.IAllService;

@Action
@Scope("prototype")
public class AllInfoAction {
	
	@Inject
	private IAllService allService;
	
	@RequestRoute("/all")
	public @ResponseJson Map<String, String> all(String name){
		System.out.println("action:"+this);
		Map<String, String> model = new HashMap<String, String>();
		System.out.println(allService);
		String value = allService.search(name);
		model.put("A", value);
		return model;
	}
	
}
