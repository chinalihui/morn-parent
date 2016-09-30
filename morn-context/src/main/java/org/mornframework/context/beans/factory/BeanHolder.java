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

package org.mornframework.context.beans.factory;

/**
 * @author Jeff.Li
 * @date 2016年9月30日
 */
public class BeanHolder {

	public static FactoryBean factoryBean;
	
	public static Object getBean(String name){
		if(factoryBean == null){
			throw new RuntimeException("Please init Web Application context!!!");
		}
		return factoryBean.getBean(name);
	}
}
