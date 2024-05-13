package com.taotao.cloud.rpc.common.idworker.enums;

/**
 * 锁常量
 */
public enum ServerSelector {
	// Redis 服务
	REDIS_SERVER(0),
	ZOOKEEPER_SERVER(1),
	CACHE_SERVER(2);

	private final Integer code;

	ServerSelector(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}
