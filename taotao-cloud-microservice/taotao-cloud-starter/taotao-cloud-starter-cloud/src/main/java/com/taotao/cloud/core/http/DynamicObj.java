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
package com.taotao.cloud.core.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.exception.BaseException;

/**
 * 动态对象动态访问 动态方式:a.@index.字段.c方式访问对象
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:13:32
 */
public class DynamicObj {

	/**
	 * jsonNode
	 */
	private JsonNode jsonNode;

	public DynamicObj(String json) {
		try {
			jsonNode = new ObjectMapper().readValue(json, JsonNode.class);
		} catch (Exception e) {
			throw new BaseException("解析json格式出错", e);
		}
	}

	/**
	 * tryParse
	 *
	 * @param path path
	 * @param cls  cls
	 * @param <T>  T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:13:52
	 */
	public <T> T tryParse(String path, Class<T> cls) {
		try {
			return parse(path, cls);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * parse
	 *
	 * @param path path
	 * @param cls  cls
	 * @param <T>  T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:14:07
	 */
	public <T> T parse(String path, Class<T> cls) {
		String[] paths = path.split("\\.");
		JsonNode node = jsonNode;
		for (String p : paths) {
			if (node != null) {
				if (p.startsWith("@")) {
					node = node.get(Integer.parseInt(p.replace("@", "")));
				} else if (node.has(p)) {
					node = node.get(p);
				} else {
					throw new BaseException("找不到path:" + path + "中" + p + "的节点值");
				}
			}
		}

		return changeType(node, cls);
	}

	/**
	 * changeType
	 *
	 * @param jsonNode jsonNode
	 * @param cls      cls
	 * @param <T>      T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:14:19
	 */
	public <T> T changeType(JsonNode jsonNode, Class<T> cls) {
		String name = cls.getSimpleName();
		if ("String".equals(name)) {
			return (T) jsonNode.asText();
		} else if ("Object".equals(name)) {
			return (T) jsonNode;
		} else if ("Long".equals(name)) {
			return (T) new Long(jsonNode.asLong());
		} else if ("Int".equals(name)) {
			return (T) new Integer(jsonNode.asInt());
		} else if ("Boolean".equals(name)) {
			return (T) new Boolean(jsonNode.asBoolean());
		}
		return null;
	}
}
