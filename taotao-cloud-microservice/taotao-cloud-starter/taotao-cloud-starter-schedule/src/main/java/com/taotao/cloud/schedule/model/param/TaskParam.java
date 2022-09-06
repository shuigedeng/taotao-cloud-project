package com.taotao.cloud.schedule.model.param;

import org.apache.commons.lang3.StringUtils;


public class TaskParam {
	//任务id
	private String id;

	//任务名
	private String name;

	/*
		目标字符串
		格式bean.method(params)
		String字符串类型，包含'、boolean布尔类型，等于true或者false
		long长整形，包含L、double浮点类型，包含D、其他类型归类为整形
	 */
	private String invokeTarget;


	//周期(month、week、day、hour、minute、secods)
	private String cycle;

	//执行策略(1手动，2-自动）
	private Integer policy;

	private String week;

	private String month;

	private String day;

	private String hour;

	private String minute;

	private String secods;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	//备注
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInvokeTarget() {
		return invokeTarget;
	}

	public void setInvokeTarget(String invokeTarget) {
		this.invokeTarget = invokeTarget;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public Integer getPolicy() {
		return policy;
	}

	public void setPolicy(Integer policy) {
		this.policy = policy;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = StringUtils.isBlank(minute) ? "0" : minute;
	}

	public String getSecods() {
		return secods;
	}

	public void setSecods(String secods) {
		this.secods = StringUtils.isBlank(secods) ? "0" : secods;
	}
}
