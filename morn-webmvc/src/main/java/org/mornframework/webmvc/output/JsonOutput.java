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
