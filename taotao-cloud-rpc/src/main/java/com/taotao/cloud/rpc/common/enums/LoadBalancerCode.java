package com.taotao.cloud.rpc.common.enums;

public enum LoadBalancerCode {
	/**
	 * 随机负载策略
	 */
	RANDOM(0),
	/**
	 * 轮询负载策略
	 */
	ROUNDROBIN(1);
	private final int code;

	LoadBalancerCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
