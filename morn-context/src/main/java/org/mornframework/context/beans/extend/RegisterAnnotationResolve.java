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

import org.mornframework.context.beans.factory.BeanDefinition;

/**
 * @author Jeff.Li
 * @date 2016年10月23日
 * 自定义注解解释器接口
 */
public interface RegisterAnnotationResolve {
	
	/**
	 * annotation Class
	 * @return
	 */
	Class<?> getAnnotationClass();
	
	/**
	 * 处理自定义注解
	 * @param beanObject
	 */
	BeanDefinition process(BeanDefinition beanDefinition);
}
