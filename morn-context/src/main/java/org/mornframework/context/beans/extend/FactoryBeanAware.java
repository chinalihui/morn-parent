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

import org.mornframework.context.beans.factory.FactoryBean;

/**
 * @author Jeff.Li
 * @date 2016年10月13日
 * 实现此接口,可在该类中获取FactoryBean
 */
public interface FactoryBeanAware {

	/**
	 * 设置FactoryBean
	 * @param factoryBean
	 */
	void setFactoryBean(FactoryBean factoryBean); 
	
}
