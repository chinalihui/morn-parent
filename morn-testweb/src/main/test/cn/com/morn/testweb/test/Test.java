package cn.com.morn.testweb.test;

import org.mornframework.context.beans.factory.ContextFactoryBean;
import org.mornframework.context.beans.factory.FactoryBean;

public class Test {

	public static void main(String[] args) {
		FactoryBean factoryBean = new ContextFactoryBean("com.morn", null);
		System.out.println(factoryBean.getBean("allInfoAction"));
		System.out.println(factoryBean.getBean("allInfoAction"));
		System.out.println(factoryBean.getBean("allInfoAction"));
	}
}
