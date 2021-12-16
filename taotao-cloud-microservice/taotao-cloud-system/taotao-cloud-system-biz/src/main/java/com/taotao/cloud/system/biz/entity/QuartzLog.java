/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;

import java.io.Serializable;
import java.sql.Timestamp;

@TableName("quartz_log")
public class QuartzLog extends SuperEntity<QuartzLog, Long> implements Serializable {

    /** 任务日志ID */
    @TableId
    private Long id;


    /** 任务名称 */
    private String baenName;


    /** cron表达式 */
    private String cronExpression;


    /** 异常详细  */
    private String exceptionDetail;


    /** 状态 */
    private Boolean isSuccess;


    /** 任务名称 */
    private String jobName;


    /** 方法名称 */
    private String methodName;


    /** 参数 */
    private String params;


    /** 耗时（毫秒） */
    private Long time;

    /** 创建日期 */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;


    public void copy(QuartzLog source) {
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

	public String getBaenName() {
		return baenName;
	}

	public void setBaenName(String baenName) {
		this.baenName = baenName;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
