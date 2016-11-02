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

package org.mornframework.context.beans.extend;

import org.mornframework.context.beans.exception.BeanInitializeException;

/**
 * @author Jeff.Li
 * @date 2016年10月13日
 * 实例化Bean之后辅助方法
 */
public interface BeanPostProcessor { 
	
	/**
	 * 多个BeanPostProcessor执行顺序
	 * @return
	 */
	int getOrder();
	
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
