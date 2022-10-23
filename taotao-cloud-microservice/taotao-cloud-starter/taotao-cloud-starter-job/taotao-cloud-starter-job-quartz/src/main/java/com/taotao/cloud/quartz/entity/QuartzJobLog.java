/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.quartz.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class QuartzJobLog implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * 任务日志ID
	 */
	private Long id;

	/**
	 * 任务id
	 */
	private Long jobId;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 任务组名
	 */
	private String jobGroup;

	/**
	 * spring bean名称
	 */
	private String beanName;

	private String className;

	/**
	 * cron表达式
	 */
	private String cronExpression;

	/**
	 * 异常详细
	 */
	private String exceptionDetail;

	/**
	 * 状态
	 */
	private Boolean isSuccess;

	/**
	 * 方法名称
	 */
	private String methodName;

	/**
	 * 参数
	 */
	private String params;

	/**
	 * 耗时（毫秒）
	 */
	private Long time;

	/**
	 * 结束时间
	 */
	private LocalDateTime endTime;

	/**
	 * 开始时间
	 */
	private LocalDateTime startTime;

	/**
	 * 执行线程
	 */
	private String executionThread;

	private LocalDateTime createTime;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

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

	public String getExceptionDetail() {
		return exceptionDetail;
	}

	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
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

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getExecutionThread() {
		return executionThread;
	}

	public void setExecutionThread(String executionThread) {
		this.executionThread = executionThread;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "QuartzLogModel{" +
			"id=" + id +
			", jobId=" + jobId +
			", jobName='" + jobName + '\'' +
			", jobGroup='" + jobGroup + '\'' +
			", beanName='" + beanName + '\'' +
			", cronExpression='" + cronExpression + '\'' +
			", exceptionDetail='" + exceptionDetail + '\'' +
			", isSuccess=" + isSuccess +
			", methodName='" + methodName + '\'' +
			", params='" + params + '\'' +
			", time=" + time +
			", endTime=" + endTime +
			", startTime=" + startTime +
			", executionThread='" + executionThread + '\'' +
			'}';
	}
}
