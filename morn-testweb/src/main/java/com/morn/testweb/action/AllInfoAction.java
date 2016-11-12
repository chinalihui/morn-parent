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

package com.morn.testweb.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mornframework.context.annotation.Action;
import org.mornframework.context.annotation.Inject;
import org.mornframework.context.annotation.Scope;
import org.mornframework.context.beans.factory.BeanHolder;
import org.mornframework.webmvc.annotation.RequestRoute;
import org.mornframework.webmvc.annotation.ResponseJson;

import com.morn.testweb.dao.IMenuDao;
import com.morn.testweb.dao.StationUserInfoDao;
import com.morn.testweb.dao.UserDao;
import com.morn.testweb.domain.User;
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
	
	@Inject
	private IMenuDao menuDao;
	
	@Inject
	private UserDao userDao;
	
	@Inject
	private StationUserInfoDao stationUserInfoDao;
	
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
	
	@RequestRoute("/menus")
	public String menus(){
		System.out.println("menuDao:" + menuDao);
		menuDao.search();
		return null;
	}
	
	@RequestRoute("/userInfo")
	@ResponseJson
	public List<User> getUserInfo(String id){
		List<User> list = new ArrayList<User>();
		list.add(userDao.findById(id));
		list.add(stationUserInfoDao.findById(id));
		return list;
	}
	
	@RequestRoute("/insertInfo")
	@ResponseJson
	public int insertInfo(String userName,String address){
		User user = new User();
		user.setUserName(userName);
		user.setAddress(address);
		return stationUserInfoDao.insertInfo(user);
	}
	
	@RequestRoute("/userAll")
	@ResponseJson
	public List<User> searchAll(String order){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("order", order);
		return stationUserInfoDao.selectAll(param);
	}
	
}
