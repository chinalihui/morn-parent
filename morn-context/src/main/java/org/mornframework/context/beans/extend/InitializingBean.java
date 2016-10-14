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

/**
 * @author Jeff.Li
 * @date 2016年10月13日
 */
public interface InitializingBean {

	/**
	 * 设置Bean属性之后执行
	 */
	void afterPropertiesSet() throws Exception; 
}
