/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 日志表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 09:25:26
 */
@Entity
@Table(name = Log.TABLE_NAME)
@TableName(Log.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Log.TABLE_NAME, comment = "日志表")
public class Log extends BaseSuperEntity<Log, Long> {

	public static final String TABLE_NAME = "tt_sys_log";

	/**
	 * 操作用户
	 */
	@Column(name = "username", nullable = false, columnDefinition = "varchar(64) not null comment '操作用户'")
	private String username;

	@Column(name = "nickname", nullable = false, columnDefinition = "varchar(64) not null comment 'nickname'")
	private String nickname;

	/**
	 * 描述
	 */
	@Column(name = "description", nullable = false, columnDefinition = "varchar(1024) not null comment '描述'")
	private String description;

	/**
	 * 方法名
	 */
	@Column(name = "method", nullable = false, columnDefinition = "varchar(64) not null comment '方法名'")
	private String method;

	@Column(name = "uid", nullable = false, columnDefinition = "bigint not null default 0 comment 'uid'")
	private Long uid;

	@Column(name = "type", nullable = false, columnDefinition = "int not null default 0 comment '类型'")
	private Integer type;

	/**
	 * 参数
	 */
	@Column(name = "params", nullable = false, columnDefinition = "varchar(1024) not null comment '参数'")
	private String params;

	/**
	 * 日志类型
	 */
	@Column(name = "log_type", nullable = false, columnDefinition = "varchar(64) not null comment '日志类型'")
	private String logType;

	/**
	 * 请求ip
	 */
	@Column(name = "request_ip", nullable = false, columnDefinition = "varchar(64) not null comment '请求ip'")
	private String requestIp;

	/**
	 * 地址
	 */
	@Column(name = "address", nullable = false, columnDefinition = "varchar(256) not null comment '地址'")
	private String address;

	/**
	 * 浏览器
	 */
	@Column(name = "browser", nullable = false, columnDefinition = "varchar(1024) not null comment '浏览器'")
	private String browser;

	/**
	 * 请求耗时
	 */
	@Column(name = "time", nullable = false, columnDefinition = "bigint not null default 0 comment '请求耗时'")
	private Long time;

	/**
	 * 异常详细
	 */
	@Column(name = "exception_detail", nullable = false, columnDefinition = "varchar(4096) not null comment '异常详细'")
	private String exceptionDetail;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getExceptionDetail() {
		return exceptionDetail;
	}

	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
}
