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

package org.mornframework.context.beans.extend;

import org.mornframework.context.beans.exception.BeanInitializeException;

/**
 * @author Jeff.Li
 * @date 2016年10月13日
 * 实例化Bean之后辅助方法
 */
public interface BeanPostProcessor { 
	
	/**
	 * 实例化Bean后,调用完set方法之后执行
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeanInitializeException
	 */
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeanInitializeException;

	/**
	 * 实例化Bean后,调用完上面方法后执行
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeanInitializeException
	 */
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeanInitializeException;
	
}
