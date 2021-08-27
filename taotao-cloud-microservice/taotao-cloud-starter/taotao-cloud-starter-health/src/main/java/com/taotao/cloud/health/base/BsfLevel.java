package com.taotao.cloud.health.base;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: huojuncheng
 * @version: 2020-10-10
 **/
public enum BsfLevel {
	//报警级别
	HIGN(3, "极其严重"),
	MIDDLE(2, "严重"),
	LOW(1, "一般"),
	;

	private int level = 1;
	private String description;

	public String getDescription() {
		return description;
	}

	public int getLevel() {
		return level;
	}

	BsfLevel(int level, String description) {
		this.description = description;
		this.level = level;
	}

	private static final Map<Integer, BsfLevel> valueLookup = new ConcurrentHashMap<>(
		values().length);

	static {
		for (BsfLevel type : EnumSet.allOf(BsfLevel.class)) {
			valueLookup.put(type.level, type);
		}
	}

	public static BsfLevel resolve(Integer code) {

		return (code != null ? valueLookup.get(code) : null);
	}

	public static String resolveName(Integer code) {
		BsfLevel mode = resolve(code);
		return mode == null ? "" : mode.getDescription();
	}
}
