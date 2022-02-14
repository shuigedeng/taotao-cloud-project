/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Scheduled日志表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Entity
@Table(name = ScheduledLog.TABLE_NAME)
@TableName(ScheduledLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ScheduledLog.TABLE_NAME, comment = "Scheduled日志表")
public class ScheduledLog extends BaseSuperEntity<ScheduledLog, Long> {

	public static final String TABLE_NAME = "tt_sys_scheduled_log";

	/**
	 * 调度器名称
	 */
	@Column(name = "scheduled_name", nullable = false, columnDefinition = "varchar(64) not null comment '调度器名称'")
	private String scheduledName;

	/**
	 * 开始时间
	 */
	@Column(name = "statr_time", nullable = false, columnDefinition = "TIMESTAMP comment '开始时间'")
	private LocalDateTime statrTime;

	/**
	 * 结束时间
	 */
	@Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP comment '结束时间'")
	private LocalDateTime endTime;

	/**
	 * 异常信息
	 */
	@Column(name = "exception", columnDefinition = "varchar(4096) comment '异常信息'")
	private String exception;

	/**
	 * 执行时间
	 */
	@Column(name = "execution_time", nullable = false, columnDefinition = "bigint default 0  comment '执行时间'")
	private Long executionTime;

	/**
	 * 是否成功
	 */
	@Column(name = "is_success", nullable = false, columnDefinition = "boolean default false comment '是否成功'")
	private Boolean isSuccess;

	/**
	 * 调度器名称
	 */
	@Column(name = "scheduled_Job", nullable = false, columnDefinition = "varchar(4096) not null comment 'scheduledJob JSON对象'")
	private String scheduledJob;

	public String getScheduledName() {
		return scheduledName;
	}

	public void setScheduledName(String scheduledName) {
		this.scheduledName = scheduledName;
	}

	public LocalDateTime getStatrTime() {
		return statrTime;
	}

	public void setStatrTime(LocalDateTime statrTime) {
		this.statrTime = statrTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
	}

	public String getScheduledJob() {
		return scheduledJob;
	}

	public void setScheduledJob(String scheduledJob) {
		this.scheduledJob = scheduledJob;
	}
}
