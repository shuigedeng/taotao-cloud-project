package com.taotao.cloud.core.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.exception.BaseException;

/**
 * @author: chejiangyi
 * @version: 2019-09-25 20:52 动态对象动态访问 动态方式:a.@index.字段.c方式访问对象
 **/
public class DynamicObj {

	JsonNode jsonNode;

	public DynamicObj(String json) {
		try {
			jsonNode = new ObjectMapper().readValue(json, JsonNode.class);
		} catch (Exception e) {
			throw new BaseException("解析json格式出错", e);
		}
	}

	public <T> T tryParse(String path, Class<T> cls) {
		try {
			return parse(path, cls);
		} catch (Exception e) {
			return null;
		}
	}

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
