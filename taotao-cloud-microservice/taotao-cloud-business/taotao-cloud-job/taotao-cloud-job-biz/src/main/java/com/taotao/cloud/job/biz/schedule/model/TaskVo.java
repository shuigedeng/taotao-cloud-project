package com.taotao.cloud.job.biz.schedule.model;


import java.util.List;

public class TaskVo {

	//任务名
	private String name;

	/**
	 * 目标字符串
	 */
	private String invokeTarget;

	/**
	 * 表达式
	 */
	private String cronExpression;

	//周期(month、week、day、hour、minute、secods)
	private String cycle;

	//执行情况(1-执行中,2-已暂停)
	private Integer situation;

	/**
	 * 下次执行时间
	 */
	private List<String> next;

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

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public List<String> getNext() {
		return next;
	}

	public void setNext(List<String> next) {
		this.next = next;
	}

	public Integer getSituation() {
		return situation;
	}

	public void setSituation(Integer situation) {
		this.situation = situation;
	}
}
