package com.morn.testweb.beans;

import org.mornframework.context.annotation.Component;
import org.mornframework.context.beans.exception.BeanInitializeException;
import org.mornframework.context.beans.extend.BeanPostProcessor;

@Component
public class SayBeanHelloBeanPostProcessor implements BeanPostProcessor{

	public int getOrder() {
		return 2;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeanInitializeException {
		System.out.println(" SayBean before beanName:" + beanName);
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeanInitializeException {
		System.out.println(" SayBean after beanName:" + beanName);
		return bean;
	}

}
