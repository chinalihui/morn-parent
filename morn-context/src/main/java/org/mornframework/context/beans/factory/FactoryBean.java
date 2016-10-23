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
 * @date 2016年9月29日
 */
public interface FactoryBean {
	
	/**
	 * 作用域为单例模式
	 */
	String SCOPE_SINGLETON = "singleton";
	
	/**
	 * 作用域为原型模式
	 */
	String SCOPE_PROTOTYPE = "prototype";
	
	/**
	 * 表示Bean为注解实例化的
	 */
	int CONTEXT_BEAN_FLAG_ANNOS = 1;
	
	/**
	 * 表示Bean为@Bean实例化的
	 */
	int CONTEXT_BEAN_FLAG_BEANS = 2;
	
	/**
	 * @param name
	 * @return
	 */
	public Object getBean(String name);
}
