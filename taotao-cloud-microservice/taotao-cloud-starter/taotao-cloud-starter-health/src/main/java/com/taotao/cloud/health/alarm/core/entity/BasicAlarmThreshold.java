package com.taotao.cloud.health.alarm.core.entity;

import java.util.List;

public class BasicAlarmThreshold {

	private String level;

	/**
	 * 启用定义的报警方式的阀值下限，
	 * <p>
	 * 当报警计数 count >= min - max 非null, count < max 则选择本报警方式; count >= max 则不选择本报警方式 - max
	 * 为null（即表示为定义时），则max赋值为  恰好大于 min 的 {@link BasicAlarmThreshold#threshold}值
	 */
	private int threshold;


	/**
	 * 报警
	 */
	private Integer max;

	private List<String> users;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
}
