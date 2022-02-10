/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.api.dto.quartz;


import java.sql.Timestamp;
import java.util.List;

public class QuartzJobQueryCriteria {

	private String jobName;

	private Boolean isSuccess;

	private Boolean isPause;

	private List<Timestamp> createTime;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
	}

	public Boolean getPause() {
		return isPause;
	}

	public void setPause(Boolean pause) {
		isPause = pause;
	}

	public List<Timestamp> getCreateTime() {
		return createTime;
	}

	public void setCreateTime(List<Timestamp> createTime) {
		this.createTime = createTime;
	}
}
