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

package com.morn.testweb.beans;

import org.mornframework.context.beans.annotation.Bean;
import org.mornframework.context.beans.annotation.Beans;
import org.mornframework.context.beans.annotation.Element;
import org.mornframework.context.beans.annotation.Entry;
import org.mornframework.context.beans.annotation.Property;

import com.morn.testweb.domain.User;
import com.morn.testweb.service.impl.TestBean;

@Beans
public class TestBeans {

	@Bean(propertys={
			@Property(name="url",value="my:8080/test-web"),
			@Property(name="timeOut",value="${timeOut}"),
			@Property(name="hosts",list={
					@Element("192.168.1.1"),
					@Element("192.168.1.2"),
					@Element("192.168.1.3"),
					@Element("192.168.1.")}),
			@Property(name="abs",map={
					@Entry(key="A",value="aaaa"),
					@Entry(key="B",value="bbbb"),
					@Entry(key="C",value="cccc")}),
			@Property(name="user",ref="user")
	})
	TestBean testBean;
	
	@Bean(propertys={
			@Property(name="userName",value="${userName}"),
			@Property(name="age",value="1")
	})
	User user;
}
