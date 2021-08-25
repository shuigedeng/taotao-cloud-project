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
package com.taotao.cloud.captcha.util;

import com.taotao.cloud.captcha.model.PointVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JsonUtil
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:52
 */
public class JsonUtil {

	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	public static List<PointVO> parseArray(String text, Class<PointVO> clazz) {
		if (text == null) {
			return null;
		} else {
			String[] arr = text.replaceFirst("\\[", "")
				.replaceFirst("\\]", "").split("\\}");
			List<PointVO> ret = new ArrayList<>(arr.length);
			for (String s : arr) {
				ret.add(parseObject(s, PointVO.class));
			}
			return ret;
		}
	}

	public static PointVO parseObject(String text, Class<PointVO> clazz) {
		if (text == null) {
			return null;
		}
		/*if(!clazz.isAssignableFrom(PointVO.class)) {
			throw new UnsupportedOperationException("不支持的输入类型:"
					+ clazz.getSimpleName());
		}*/
		try {
			PointVO ret = clazz.newInstance();
			return ret.parse(text);
		} catch (Exception ex) {
			logger.error("json解析异常", ex);

		}
		return null;
	}

	public static String toJSONString(Object object) {
		if (object == null) {
			return "{}";
		}
		if (object instanceof PointVO) {
			PointVO t = (PointVO) object;
			return t.toJsonString();
		}
		if (object instanceof List) {
			List<PointVO> list = (List<PointVO>) object;
			StringBuilder buf = new StringBuilder("[");
			list.stream().forEach(t -> {
				buf.append(t.toJsonString()).append(",");
			});
			return buf.deleteCharAt(buf.lastIndexOf(",")).append("]").toString();
		}
		if (object instanceof Map) {
			return ((Map) object).entrySet().toString();
		}
		throw new UnsupportedOperationException("不支持的输入类型:"
			+ object.getClass().getSimpleName());
	}
}
