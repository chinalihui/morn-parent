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

package org.mornframework.context.beans.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jeff.Li
 * @date 2016年10月9日
 * bean中有属性为java.util.Map时使用
 */
@Target({ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Entry {
	
	/**
	 * 对应Map中Key(暂只支持基本类型)
	 * @return
	 */
	String key();
	
	/**
	 * 对应Map中Value(暂只支持基本类型)
	 * @return
	 */
	String value();
}