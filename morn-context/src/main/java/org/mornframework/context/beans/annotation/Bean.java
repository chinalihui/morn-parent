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

import org.mornframework.context.beans.factory.FactoryBean;

/**
 * @author Jeff.Li
 * @date 2016年10月9日
 * 定义上下文bean
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

	/**
	 * bean名称,定义唯一名(默认取定义属性名)
	 * @return
	 */
	String name() default "";
	
	/**
	 * bean对应的class
	 * @return
	 */
	String classes() default "";
	
	/**
	 * bean的作用域
	 * @return
	 */
	String scope() default FactoryBean.SCOPE_SINGLETON;

	/**
	 * bean的父级bean名称
	 * @return
	 */
	String parent() default "";

	/**
	 * bean类中的属性
	 * @return
	 */
	Property[] propertys() default {};
	
	/**
	 * 备注
	 * @return
	 */
	String remark() default "";

}