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

import org.mornframework.context.beans.annotation.Element;
import org.mornframework.context.beans.annotation.Entry;
 
/**
 * @author Jeff.Li
 * @date 2016年10月11日
 * Bean对应属性的基本描述
 */
public class PropertyDefinition {

	private String name;
	private String value;
	private String ref;
	private Element[] list;
	private Entry[] map;
	
	public PropertyDefinition(String name, String value, String ref,
			Element[] list, Entry[] map) {
		super();
		this.name = name;
		this.value = value;
		this.ref = ref;
		this.list = list;
		this.map = map;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public Element[] getList() {
		return list;
	}
	public void setList(Element[] list) {
		this.list = list;
	}
	public Entry[] getMap() {
		return map;
	}
	public void setMap(Entry[] map) {
		this.map = map;
	}
}
