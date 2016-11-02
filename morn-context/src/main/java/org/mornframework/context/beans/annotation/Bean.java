/**
 * Copyright 2016- the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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