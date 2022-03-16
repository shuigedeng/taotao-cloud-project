/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity.quartz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Quartz任务表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Entity
@Table(name = QuartzJob.TABLE_NAME)
@TableName(QuartzJob.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = QuartzJob.TABLE_NAME, comment = "Quartz任务表")
public class QuartzJob extends BaseSuperEntity<QuartzJob, Long> {

	public static final String TABLE_NAME = "tt_quartz_job";

	/**
	 * Spring Bean名称
	 */
	@Column(name = "bean_name", nullable = false, columnDefinition = "varchar(64) not null comment ' Spring Bean名称'")
	private String beanName;

	/**
	 * cron 表达式
	 */
	@Column(name = "cron_expression", nullable = false, columnDefinition = "varchar(64) not null comment 'cron 表达式'")
	private String cronExpression;

	/**
	 * 状态：1暂停、0启用
	 */
	@Column(name = "is_pause", nullable = false, columnDefinition = "boolean DEFAULT false comment '收件人'")
	private Boolean isPause;

	/**
	 * 任务名称
	 */
	@Column(name = "job_name", nullable = false, columnDefinition = "varchar(64) not null comment '任务名称'")
	private String jobName;

	/**
	 * 方法名称
	 */
	@Column(name = "method_name", nullable = false, columnDefinition = "varchar(64) not null comment '方法名称'")
	private String methodName;

	/**
	 * 参数
	 */
	@Column(name = "params", nullable = false, columnDefinition = "varchar(64) not null comment '参数'")
	private String params;

	/**
	 * 备注
	 */
	@Column(name = "remark", nullable = false, columnDefinition = "varchar(256) not null comment '备注'")
	private String remark;

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
}
