/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.bigdata.flume.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * AccessLogInterceptor
 *
 * @author dengtao
 * @version v1.0.0
 * @since 2020/9/22 13:30
 */
public class AccessLogInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(AccessLogInterceptor.class);

	private static final List<String> PREFIX = Arrays.asList("%", "_", "$");

	// private JSONObject metaJson;

	@Override
	public void initialize() {
		// metaJson = MetaUtils.getLogMetaJson();
	}

	@Override
	public Event intercept(Event event) {
		byte[] body = event.getBody();
		String bodyStr = new String(body);
		try {
			JSONObject result = new JSONObject();
			String[] fields = bodyStr.split("-");
			if (fields.length == 2) {
				String metaData = fields[0];
				String logData = fields[1];

				String metaStr = new String(Base64.getDecoder().decode(metaData));
				JSONObject metaJson = JSONObject.parseObject(metaStr);

				String logStr = new String(Base64.getDecoder().decode(logData));
				JSONObject logJson = JSONObject.parseObject(logStr);

				JSONObject propertiesJson = logJson.getJSONObject("properties");
				JSONObject libJson = logJson.getJSONObject("lib");
				logJson.remove("properties");
				logJson.remove("lib");
				result.putAll(logJson);
				result.putAll(propertiesJson);
				result.putAll(libJson);

				JSONObject jsonObject = new JSONObject();
				Iterator<Map.Entry<String, Object>> iterator = result.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, Object> next = iterator.next();
					String key = next.getKey();

					String one = String.valueOf(key.charAt(0));
					if (PREFIX.contains(one)) {
						iterator.remove();
						jsonObject.put(key.substring(1), next.getValue());
					} else {
						jsonObject.put(key, next.getValue());
					}
				}
				result.putAll(jsonObject);

				if (null != metaJson) {
					result.putAll(metaJson);
				}

				String logday = new SimpleDateFormat("yyyy-MM-dd")
					.format(Double.parseDouble(metaJson.getString("ctime")));
				event.getHeaders().put("logday", logday);
				event.getHeaders().put("source", metaJson.getString("source"));

				logger.info("********************>" + result.toJSONString());
				event.setBody(result.toJSONString().getBytes());
				return event;
			}
		} catch (Exception e) {
			try {
				String bodyData = new String(Base64.getDecoder().decode(bodyStr));
				JSONObject bodyJson = JSONObject.parseObject(bodyData);
				String logday = new SimpleDateFormat("yyyy-MM-dd")
					.format(Double.parseDouble(bodyJson.getString("ctime")));
				event.getHeaders().put("logday", logday);
				event.getHeaders().put("source", bodyJson.getString("source"));

				logger.info("====================>" + bodyJson.toJSONString());
				event.setBody(bodyJson.toJSONString().getBytes());
				return event;
			} catch (Exception error) {
				error.printStackTrace();
				logger.error(error.getMessage());
				return null;
			}
		}

		return event;
	}

	@Override
	public List<Event> intercept(List<Event> list) {
		List<Event> result = new ArrayList<>();
		for (Event event : list) {
			Event intercept = intercept(event);
			if (null != intercept) {
				result.add(intercept);
			}
		}
		return result;
	}

	@Override
	public void close() {

	}

	public static class Builder implements Interceptor.Builder {

		@Override
		public Interceptor build() {
			return new AccessLogInterceptor();
		}

		@Override
		public void configure(Context context) {

		}
	}
}
