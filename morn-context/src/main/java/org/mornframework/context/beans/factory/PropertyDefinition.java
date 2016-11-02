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
	
	public PropertyDefinition(){}
	
	public PropertyDefinition(String name,String value,String ref){
		this.name = name;
		this.value = value;
		this.ref = ref;
	}
	
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
