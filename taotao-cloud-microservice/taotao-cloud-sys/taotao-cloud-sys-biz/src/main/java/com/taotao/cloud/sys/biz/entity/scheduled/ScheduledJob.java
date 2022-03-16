/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity.scheduled;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.schedule.enums.ScheduledType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Scheduled任务表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Entity
@Table(name = ScheduledJob.TABLE_NAME)
@TableName(ScheduledJob.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ScheduledJob.TABLE_NAME, comment = "Scheduled任务表")
public class ScheduledJob extends BaseSuperEntity<ScheduledJob, Long> {

	public static final String TABLE_NAME = "tt_scheduled_job";

	/**
	 * cron表达式
	 */
	@Column(name = "cron", nullable = false, columnDefinition = "varchar(64) not null comment 'cron表达式'")
	private String cron;

	/**
	 * 时区，cron表达式会基于该时区解析
	 */
	@Column(name = "zone", nullable = false, columnDefinition = "varchar(64) not null comment '时区，cron表达式会基于该时区解析'")
	private String zone;

	/**
	 * 上一次执行完毕时间点之后多长时间再执行
	 */
	@Column(name = "fixed_delay", nullable = false, columnDefinition = "bigint not null default 0 comment '上一次执行完毕时间点之后多长时间再执行'")
	private Long fixedDelay;

	/**
	 * 与 fixedDelay 意思相同，只是使用字符串的形式
	 */
	@Column(name = "fixed_delay_string", nullable = false, columnDefinition = "varchar(64) not null comment '与 fixedDelay 意思相同，只是使用字符串的形式'")
	private String fixedDelayString;

	/**
	 * 上一次开始执行时间点之后多长时间再执行
	 */
	@Column(name = "fixed_rate", nullable = false, columnDefinition = "bigint not null default 0 comment '上一次开始执行时间点之后多长时间再执行'")
	private Long fixedRate;

	/**
	 * 与 fixedRate 意思相同，只是使用字符串的形式
	 */
	@Column(name = "fixed_rate_string", nullable = false, columnDefinition = "varchar(64) not null comment '与 fixedRate 意思相同，只是使用字符串的形式'")
	private String fixedRateString;

	/**
	 * 第一次延迟多长时间后再执行
	 */
	@Column(name = "initial_delay", nullable = false, columnDefinition = "bigint not null default 0 comment '第一次延迟多长时间后再执行'")
	private Long initialDelay;

	/**
	 * 与 initialDelay 意思相同，只是使用字符串的形式
	 */
	@Column(name = "initial_delay_string", nullable = false, columnDefinition = "varchar(64) not null comment '与 initialDelay 意思相同，只是使用字符串的形式'")
	private String initialDelayString;

	/**
	 * 任务是否已终止
	 */
	@Column(name = "cancel", nullable = false, columnDefinition = "boolean default false comment '任务是否已终止'")
	private boolean cancel;

	/**
	 * 执行次数
	 */
	@Column(name = "num", nullable = false, columnDefinition = "int default 0  comment '执行次数'")
	private int num;

	/**
	 * 方法名称
	 */
	@Column(name = "method_name", nullable = false, columnDefinition = "varchar(64) not null comment '方法名称'")
	private String methodName;

	/**
	 * 执行次数
	 */
	@Column(name = "bean_name", nullable = false, columnDefinition = "varchar(64) not null comment ' Spring Bean名称'")
	private String beanName;

	/**
	 * 类型
	 *
	 * @see ScheduledType
	 */
	@Column(name = "type", nullable = false, columnDefinition = "varchar(64) not null comment '类型'")
	private String type;

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Long getFixedDelay() {
		return fixedDelay;
	}

	public void setFixedDelay(Long fixedDelay) {
		this.fixedDelay = fixedDelay;
	}

	public String getFixedDelayString() {
		return fixedDelayString;
	}

	public void setFixedDelayString(String fixedDelayString) {
		this.fixedDelayString = fixedDelayString;
	}

	public Long getFixedRate() {
		return fixedRate;
	}

	public void setFixedRate(Long fixedRate) {
		this.fixedRate = fixedRate;
	}

	public String getFixedRateString() {
		return fixedRateString;
	}

	public void setFixedRateString(String fixedRateString) {
		this.fixedRateString = fixedRateString;
	}

	public Long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(Long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public String getInitialDelayString() {
		return initialDelayString;
	}

	public void setInitialDelayString(String initialDelayString) {
		this.initialDelayString = initialDelayString;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
