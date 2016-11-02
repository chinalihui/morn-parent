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

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class ErrorOutput extends Output{

	protected final String contentType = "text/html; charset=" + getEncoding();
	
	protected final String html404 = "<html><head><title>404 Not Found</title></head><body bgcolor='white'><center><h1>HTTP Status 404 Not Found URL[_VIEW_]</h1></center></body></html>";
	protected final String html405 = "<html><head><title>405 Request Method not supported</title></head><body bgcolor='white'><center><h1>HTTP Status 405 - Request method <METHOD> not supported</h1></center></body></html>";
	protected final String html500 = "<html><head><title>500 Internal Server Error</title></head><body bgcolor='white'><center><h1>HTTP Status 500 Internal Server Error:</h1><h3><MSG></h3></center></body></html>";
	
	protected final String html401 = "<html><head><title>401 Unauthorized</title></head><body bgcolor='white'><center><h1>401 Unauthorized</h1></center></body></html>";
	protected final String html403 = "<html><head><title>403 Forbidden</title></head><body bgcolor='white'><center><h1>403 Forbidden</h1></center></body></html>";
	
	protected int errorCode;
	private Exception e;
	
	public ErrorOutput(int errorCode,String url){
		this.errorCode = errorCode;
		this.view = url;
	}
	
	public ErrorOutput(int errorCode,String url,Exception e){
		this.e = e;
		this.errorCode = errorCode;
		this.view = url;
	}
	
	@Override
	public void output() {
		response.setStatus(errorCode);
		
		PrintWriter writer = null;
		try {
			response.setContentType(contentType);
	        writer = response.getWriter();
	        writer.write(getErrorHtml());
	        writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public String getErrorHtml() {
		if (errorCode == 404)
			return html404.replace("_VIEW_", view);
		if(errorCode == 405)
			return html405.replace("<METHOD>", request.getMethod());
		if (errorCode == 500){
			String msg = e.getMessage();
			if(StringUtils.isEmpty(msg)){
				msg = e.getCause().getMessage();
			}
			msg = msg == null ? "" : msg;
			return html500.replace("<MSG>", msg);
		}
		if (errorCode == 401)
			return html401;
		if (errorCode == 403)
			return html403;
		return "<html><head><title>" + errorCode + " Error</title></head><body bgcolor='white'><center><h1>" + errorCode + " Error</h1></center></body></html>";
	}

}
