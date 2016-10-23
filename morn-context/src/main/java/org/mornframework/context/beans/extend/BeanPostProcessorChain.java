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
