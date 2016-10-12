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
 * bean属性
 */
@Target({ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Property {

	/**
	 * 属性名
	 * @return
	 */
	String name();
	
	/**
	 * 值(包含基本类型,与ref先取value中的值)
	 * @return
	 */
	String value() default "";
	
	/**
	 * 引用的bean
	 * @return
	 */
	String ref() default "";
	
	/**
	 * 属性为java.util.List集合时用此注入值
	 * @return
	 */
	Element[] list() default {};
	
	/**
	 * 属性为java.util.Map时用此注入值
	 * @return
	 */
	Entry[] map() default {};
	
}
