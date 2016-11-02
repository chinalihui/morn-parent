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
