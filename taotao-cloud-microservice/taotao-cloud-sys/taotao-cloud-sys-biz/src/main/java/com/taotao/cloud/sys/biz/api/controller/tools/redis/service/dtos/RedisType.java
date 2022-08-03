package com.taotao.cloud.sys.biz.api.controller.tools.redis.service.dtos;

/**
 * redis 的数据类型
 */
public enum RedisType {
	string("string"),
	Set("set"),
	ZSet("zset"),
	Hash("hash"),
	List("list"),
	None("none");

	private String value;

	RedisType(String value) {
		this.value = value;
	}

	public static RedisType parse(String type) {
		RedisType[] values = RedisType.values();
		for (RedisType value : values) {
			if (value.value.equals(type)) {
				return value;
			}
		}
		return None;
	}

	public String getValue() {
		return value;
	}
}
