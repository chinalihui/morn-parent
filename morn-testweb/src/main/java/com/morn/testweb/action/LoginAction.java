package com.morn.testweb.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mornframework.webmvc.annotation.Action;
import org.mornframework.webmvc.annotation.Json;
import org.mornframework.webmvc.annotation.Req;
import org.mornframework.webmvc.support.ModelAndView;

import com.morn.testweb.domain.User;

@Action
@Req("/login")
public class LoginAction{

	@Req("/hello")
	public String hello(String username,String password,HttpServletRequest request,int age){
		System.out.println("LoginAction.hello()" + new Timestamp(System.currentTimeMillis()));
		System.out.println("username:"+username+"\t password:"+password + "\t age:"+age);
		System.out.println("request username:"+request.getParameter("username"));
		return "ok.jsp";
	}
	
	@Req("/open")
	public void open(HttpServletResponse response,String text,int ms){
		System.out.println(this + "\t" + Thread.currentThread().getName());
		try {
			try {
				if(ms > 0){
					Thread.sleep(ms * 1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			PrintWriter pw = response.getWriter();
			pw.write("text:"+text);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Req("/say")
	public String say(String text){
		System.out.println("text:"+text);
		return "redirect:hello";
	}
	
	@Req("/sayJson")
	public @Json Map<String, Object> sayJson(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Jeff.Li");
		map.put("address", "ShangHai");
		map.put("now", new Date());
		map.put("language", "中文");
		return map;
	}
	
	@Req("/toIndex")
	public ModelAndView toIndex(){
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("year", 2016);
		model.put("month", 9);
		
		User user = new User();
		user.setUserName("Jeff.Li");
		user.setAddress("ShangHai");
		user.setAge(10);
		return new ModelAndView("info.jsp", model).addObject("user", user);
	}
	
}
