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

/**
 * @author Jeff.Li
 * @date 2016年10月23日
 */
public class BeanPostProcessorChain implements Comparable<BeanPostProcessorChain>{
	
	/**
	 * 执行顺序
	 */
	private int order;
	private BeanPostProcessor beanPostProcessor;
	
	public BeanPostProcessorChain(int order, BeanPostProcessor beanPostProcessor) {
		super();
		this.order = order;
		this.beanPostProcessor = beanPostProcessor;
	}

	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public BeanPostProcessor getBeanPostProcessor() {
		return beanPostProcessor;
	}
	public void setBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		this.beanPostProcessor = beanPostProcessor;
	}
	
	public int compareTo(BeanPostProcessorChain b) {
		return this.order - b.getOrder();
	}

}
