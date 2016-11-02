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

package org.mornframework.context.beans.factory;

/**
 * @author Jeff.Li
 * @date 2016年9月29日
 */
public interface FactoryBean {
	
	/**
	 * 作用域为单例模式
	 */
	String SCOPE_SINGLETON = "singleton";
	
	/**
	 * 作用域为原型模式
	 */
	String SCOPE_PROTOTYPE = "prototype";
	
	/**
	 * 表示Bean为注解实例化的
	 */
	int CONTEXT_BEAN_FLAG_ANNOS = 1;
	
	/**
	 * 表示Bean为@Bean实例化的
	 */
	int CONTEXT_BEAN_FLAG_BEANS = 2;
	
	/**
	 * @param name
	 * @return
	 */
	public Object getBean(String name);
}
