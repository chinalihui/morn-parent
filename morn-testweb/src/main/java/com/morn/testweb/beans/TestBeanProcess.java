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
