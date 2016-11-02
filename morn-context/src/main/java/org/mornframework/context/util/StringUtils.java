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

package org.mornframework.context.util;

public class StringUtils extends org.apache.commons.lang.StringUtils {

	public static String firstToLowerCase(String str){
		return str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toLowerCase());
	}
	
	public static String firstToUpperCase(String str){
		return str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toUpperCase());
	}
	
	public static boolean includeProperty(String propertyName){
		if(isEmpty(propertyName)){
			return false;
		}
		return propertyName.startsWith("${") && propertyName.endsWith("}");
	}
	
	public static String propertyKey(String propertyName){
		if(isEmpty(propertyName)){
			return propertyName;
		}
		return propertyName.replace("${", "").replace("}", "");
	}
	
}
