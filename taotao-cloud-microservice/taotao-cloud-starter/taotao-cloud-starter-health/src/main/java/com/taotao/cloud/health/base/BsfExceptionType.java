package com.taotao.cloud.health.base;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BsfExceptionType class 异常级别
 *
 * @author huojuncheng
 */
public enum BsfExceptionType {
	BE(1, "后端"),
	FE(2, "前端"),
	BUSINESS(3, "业务"),
	OPERATION(4, "运维"),
	DBA(5, "DBA"),
	ELSE(6, "其他");

	private int code;
	private String name;

	BsfExceptionType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	private static final Map<Integer, BsfExceptionType> valueLookup = new ConcurrentHashMap<>(
		values().length);

	static {
		for (BsfExceptionType type : EnumSet.allOf(BsfExceptionType.class)) {
			valueLookup.put(type.code, type);
		}
	}

	public static BsfExceptionType resolve(Integer code) {

		return (code != null ? valueLookup.get(code) : null);
	}

	public static String resolveName(Integer code) {
		BsfExceptionType mode = resolve(code);
		return mode == null ? "" : mode.getName();
	}
}
