/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;
import java.sql.Timestamp;

@TableName("scheduled_job")
public class ScheduledJob extends SuperEntity<ScheduledJob, Long> implements Serializable {

	public static final String JOB_KEY = "JOB_KEY";

	/**
	 * 定时任务ID
	 */
	@TableId
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
	@TableField(fill = FieldFill.INSERT)
	private Timestamp createTime;

	public void copy(ScheduledJob source) {
		BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
