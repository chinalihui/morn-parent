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
package org.mornframework.webmvc.output;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mornframework.webmvc.context.ContextCommons;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public abstract class Output {
	
	protected String view;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public static String getEncoding() {
		return ContextCommons.DEFAULT_ENCODING;
	}
	
	public Output setOutput(HttpServletRequest request,
			HttpServletResponse response){
		this.request = request;
		this.response = response;
		return this;
	}

	public abstract void output();
}
