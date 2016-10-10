package com.morn.testweb.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mornframework.context.annotation.Action;
import org.mornframework.context.annotation.Value;
import org.mornframework.webmvc.annotation.RequestRoute;
import org.mornframework.webmvc.annotation.ResponseJson;
import org.mornframework.webmvc.support.ModelAndView;
import org.mornframework.webmvc.support.RequestMethod;

import com.morn.testweb.domain.User;

@Action
@RequestRoute("/login")
public class LoginAction{

	@RequestRoute("/hello")
	public String hello(String username,String password,HttpServletRequest request,int age){
		System.out.println("LoginAction.hello()" + new Timestamp(System.currentTimeMillis()));
		System.out.println("username:"+username+"\t password:"+password + "\t age:"+age);
		System.out.println("request username:"+request.getParameter("username"));
		return "ok.jsp";
	}
	
	@RequestRoute("/open")
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
	
	@RequestRoute(value="/say",method=RequestMethod.POST)
	public String say(String text){
		System.out.println("text:"+text);
		return "redirect:hello";
	}
	
	@Value("app.name")
	private String appName;
	
	@Value("author")
	private String author;
	
	@RequestRoute("/sayJson")
	public @ResponseJson Map<String, Object> sayJson(@Value("app.url") String url){
		System.out.println("appName:"+appName + "\t author:"+author + "\t url:" + url);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Jeff.Li");
		map.put("address", "ShangHai");
		map.put("now", new Date());
		map.put("language", "中文");
		return map;
	}
	
	@RequestRoute("/toIndex")
	public ModelAndView toIndex(String num){
		System.out.println(this);
		Integer.parseInt(num);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("year", 2016);
		model.put("month", 9);
		
		User user = new User();
		user.setUserName("Jeff.Li");
		user.setAddress("ShangHai");
		user.setAge(10);
		return new ModelAndView("info.jsp", model).addObject("user", user);
	}
	
	@RequestRoute("/ajaxReq")
	@ResponseJson
	public Map<String, Object> ajaxReq(HttpServletRequest request, String username){
		System.out.println("username:"+username);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("result", "success:"+username);
		return model;
	}
	
	@RequestRoute("/submitForm")
	public String submitForm(HttpServletRequest request,User user,String flag){
		System.out.println(user);
		System.out.println(flag);
		return "ok.jsp";
	}
	
}
