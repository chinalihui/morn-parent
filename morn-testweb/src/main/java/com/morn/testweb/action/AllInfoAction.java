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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.mornframework.context.annotation.Action;
import org.mornframework.context.annotation.Inject;
import org.mornframework.context.annotation.Scope;
import org.mornframework.context.beans.factory.BeanHolder;
import org.mornframework.webmvc.annotation.RequestRoute;
import org.mornframework.webmvc.annotation.ResponseJson;

import com.morn.testweb.service.IAllService;
import com.morn.testweb.service.impl.TestBean;

@Action
@Scope("prototype")
public class AllInfoAction {
	
	@Inject
	private IAllService allService;
	
	@Inject
	private DataSource core_oracle_ds_rw;
	
	@Inject
	private TestBean testBean;
	
	@RequestRoute("/all")
	public @ResponseJson Map<String, String> all(String name){
		System.out.println("action:"+this);
		System.out.println("action:"+BeanHolder.getBean("allInfoAction"));
		Map<String, String> model = new HashMap<String, String>();
		System.out.println(allService);
		String value = allService.search(name);
		model.put("A", value);
		return model;
	}
	
	
	@RequestRoute("/search")
	public String search(String text)throws Exception{
		Connection connection = core_oracle_ds_rw.getConnection();
		PreparedStatement ps = connection.prepareStatement(" select code,value from t_exp_config ");
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			System.out.println(rs.getString("code") + "\t" + rs.getString("value"));
		}
		rs.close();
		ps.close();
		connection.close();
		return null;
	}
	
	@RequestRoute("/lookBean")
	public String lookTestBean(){
		System.out.println(testBean.getUrl());
		System.out.println(testBean.getTimeOut());
		System.out.println(testBean.getHosts());
		System.out.println(testBean.getAbs());
		System.out.println(testBean.getUser().getUserName() + "\t" + testBean.getUser().getAge());
		return null;
	}
	
}
