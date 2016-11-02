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
