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

import org.mornframework.webmvc.annotation.Action;
import org.mornframework.webmvc.annotation.Json;
import org.mornframework.webmvc.annotation.Req;

@Action
public class AllInfoAction {

	@Req("/all")
	public @Json Map<String, String> all(){
		Map<String, String> model = new HashMap<String, String>();
		model.put("A", "xxxxxxx");
		return model;
	}
	
}
