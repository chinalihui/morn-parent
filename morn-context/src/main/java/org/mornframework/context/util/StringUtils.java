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

package org.mornframework.context.util;

public class StringUtils extends org.apache.commons.lang.StringUtils {

	public static String firstToLowerCase(String str){
		return str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toLowerCase());
	}
	
	public static String firstToUpperCase(String str){
		return str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toUpperCase());
	}
	
}
