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

package org.mornframework.context.beans.factory;

import java.util.Map;

/**
 * @author Jeff.Li
 * @date 2016年10月11日
 * Bean的基本描述
 */
public class BeanDefinition {
	
	public BeanDefinition(){}
	
	public BeanDefinition(String beanName,String beanClass){
		this.beanName = beanName;
		this.beanClass = beanClass;
	}
	
	private String beanName;
	private String beanClass;
	private String scope;
	private String parent;
	private Map<String, PropertyDefinition> propertys;
	private String remark;
	
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getBeanClass() {
		return beanClass;
	}
	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Map<String, PropertyDefinition> getPropertys() {
		return propertys;
	}

	public void setPropertys(Map<String, PropertyDefinition> propertys) {
		this.propertys = propertys;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
