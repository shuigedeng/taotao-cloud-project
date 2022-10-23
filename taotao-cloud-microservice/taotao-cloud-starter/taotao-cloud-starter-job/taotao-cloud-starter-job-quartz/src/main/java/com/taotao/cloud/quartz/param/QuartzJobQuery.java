package com.taotao.cloud.quartz.param;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 定时任务
 */
@Accessors(chain = true)
@Schema(title = "定时任务")
public class QuartzJobQuery extends PageParam {

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "任务名称")
	private String jobName;

	@Schema(description = "任务组名称")
	private String groupName;

	@Schema(description = "Bean名称")
	private String beanName;

	@Schema(description = "任务类名 和 bean名称 互斥")
	private String jobClassName;

	@Schema(description = "cron表达式")
	private String cronExpression;

	@Schema(description = "方法名称")
	private String methodName;

	@Schema(description = "参数")
	private String params;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "是否并发  0：禁止  1：允许")
	private Integer concurrent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
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

	public Integer getConcurrent() {
		return concurrent;
	}

	public void setConcurrent(Integer concurrent) {
		this.concurrent = concurrent;
	}
}
