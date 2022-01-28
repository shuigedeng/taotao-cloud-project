/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.web.quartz;

import java.time.LocalDateTime;

public class QuartzJobModel {

	public static final String JOB_KEY = "JOB_KEY";

	/**
	 * 定时任务ID
	 */
	private Long id;

	/**
	 * Spring Bean名称
	 */
	private String beanName;

	/**
	 * cron 表达式
	 */
	private String cronExpression;

	/**
	 * 状态：1暂停、0启用
	 */
	private Boolean isPause;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 方法名称
	 */
	private String methodName;

	/**
	 * 参数
	 */
	private String params;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Boolean getPause() {
		return isPause;
	}

	public void setPause(Boolean pause) {
		isPause = pause;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
}
