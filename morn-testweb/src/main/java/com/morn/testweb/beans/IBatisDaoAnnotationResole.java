package com.morn.testweb.beans;

import org.mornframework.context.annotation.Component;
import org.mornframework.context.beans.extend.FactoryBeanAware;
import org.mornframework.context.beans.extend.RegisterAnnotationResolve;
import org.mornframework.context.beans.factory.BeanDefinition;
import org.mornframework.context.beans.factory.FactoryBean;
import org.mornframework.context.beans.factory.PropertyDefinition;

@Component
public class IBatisDaoAnnotationResole implements RegisterAnnotationResolve,FactoryBeanAware{

	public Class<?> annotationClass = IBatisDao.class;

	public FactoryBean factoryBean;
	
	public Class<?> getAnnotationClass() {
		return annotationClass;
	}

	public BeanDefinition process(BeanDefinition beanDefinition) {
		System.out.println(beanDefinition.getBeanName() + "\t factoryBean:" + factoryBean);
		beanDefinition.setBeanFlag(FactoryBean.CONTEXT_BEAN_FLAG_BEANS);
		IBatisDao iBatisDao = beanDefinition.getBeanClass().getAnnotation(IBatisDao.class);
		
		PropertyDefinition propertyDefinition = new PropertyDefinition(null, null, iBatisDao.value());
		beanDefinition.getPropertys().put("dataSource", propertyDefinition);
		return beanDefinition;
	}

	public void setFactoryBean(FactoryBean factoryBean) {
		this.factoryBean = factoryBean;
	}

}
