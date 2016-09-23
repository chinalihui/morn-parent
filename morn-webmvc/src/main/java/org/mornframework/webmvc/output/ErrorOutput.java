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
package org.mornframework.webmvc.output;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class ErrorOutput extends Output{

	protected final String contentType = "text/html; charset=" + getEncoding();
	
	protected final String html404 = "<html><head><title>404 Not Found</title></head><body bgcolor='white'><center><h1>404 Not Found URL[_VIEW_]</h1></center></body></html>";
	protected final String html500 = "<html><head><title>500 Internal Server Error</title></head><body bgcolor='white'><center><h1>500 Internal Server Error</h1></center></body></html>";
	
	protected final String html401 = "<html><head><title>401 Unauthorized</title></head><body bgcolor='white'><center><h1>401 Unauthorized</h1></center></body></html>";
	protected final String html403 = "<html><head><title>403 Forbidden</title></head><body bgcolor='white'><center><h1>403 Forbidden</h1></center></body></html>";
	
	protected int errorCode;
	
	public ErrorOutput(int errorCode,String url){
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
		if (errorCode == 500)
			return html500;
		if (errorCode == 401)
			return html401;
		if (errorCode == 403)
			return html403;
		return "<html><head><title>" + errorCode + " Error</title></head><body bgcolor='white'><center><h1>" + errorCode + " Error</h1></center></body></html>";
	}

}
