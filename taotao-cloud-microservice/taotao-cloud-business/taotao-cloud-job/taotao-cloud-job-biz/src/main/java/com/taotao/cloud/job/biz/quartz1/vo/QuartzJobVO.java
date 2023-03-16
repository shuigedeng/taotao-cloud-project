package com.taotao.cloud.job.biz.quartz1.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 定时任务
 */
@Schema(title = "定时任务")
public class QuartzJobVO {

	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 任务类名
	 */
	private String jobClassName;

	/**
	 * cron表达式
	 */
	private String cron;

	/**
	 * 参数
	 */
	private String parameter;

	/**
	 * 状态
	 *
	 * @see cn.bootx.starter.quartz.code.QuartzJobCode
	 */
	private Integer state;

	/**
	 * 备注
	 */
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
