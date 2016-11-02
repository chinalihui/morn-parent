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

import org.mornframework.context.annotation.Component;
import org.mornframework.context.beans.exception.BeanInitializeException;
import org.mornframework.context.beans.extend.BeanPostProcessor;
import org.mornframework.context.beans.extend.FactoryBeanAware;
import org.mornframework.context.beans.extend.InitializingBean;
import org.mornframework.context.beans.extend.annotation.InitMethod;
import org.mornframework.context.beans.factory.FactoryBean;

@Component
public class TestBeanProcess implements BeanPostProcessor,InitializingBean,FactoryBeanAware{

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeanInitializeException {
		System.out.println(" TestBeanProcessor before beanName:" + beanName);
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeanInitializeException {
		System.out.println(" TestBeanProcessor after beanName:" + beanName);
		return bean;
	}

	public void afterPropertiesSet() throws Exception {
		System.out.println("==============:afterPropertiesSet");
	}

	public void setFactoryBean(FactoryBean factoryBean) {
		System.out.println("factoryBean:" + factoryBean);
	}
	
	@InitMethod
	public void init(){
		System.out.println("TestBeanProcess.init()");
	}

	public int getOrder() {
		return 1;
	}

}
