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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 * @author Jeff.Li
 * @date 2016年9月20日
 */
public class JsonOutput extends Output{
	
	private static String dateFormat;
	private static SerializeConfig mapping = new SerializeConfig();
	static {
	    dateFormat = "yyyy-MM-dd HH:mm:ss";
	    mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
	}
	
	protected Object result;
	
	public JsonOutput(Object result){
		this.result = result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void output() {
		if(result == null){
			result = new ArrayList();
		}
		
		StringBuffer strb = new  StringBuffer();
		if (result instanceof Collection<?>)
			strb.append(JSONArray.toJSONString(result,mapping,SerializerFeature.WriteDateUseDateFormat));
		else
			strb.append(JSONObject.toJSONString(result,mapping,SerializerFeature.WriteDateUseDateFormat));
		
		PrintWriter out;
		try {
			response.setCharacterEncoding(getEncoding());
			response.setHeader("content-type","text/html;charset=UTF-8");
			
			out = response.getWriter();
			out.write(strb.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
