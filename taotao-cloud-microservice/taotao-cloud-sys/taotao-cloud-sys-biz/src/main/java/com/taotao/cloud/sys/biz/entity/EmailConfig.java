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
 * 邮件配置表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Entity
@Table(name = EmailConfig.TABLE_NAME)
@TableName(EmailConfig.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = EmailConfig.TABLE_NAME, comment = "邮件配置表")
public class EmailConfig extends BaseSuperEntity<EmailConfig, Long> {

	public static final String TABLE_NAME = "tt_sys_email_config";


	/**
	 * 收件人
	 */
	@Column(name = "from_user", nullable = false, columnDefinition = "varchar(64) not null comment '收件人'")
	private String fromUser;


	/**
	 * 邮件服务器SMTP地址
	 */
	@Column(name = "host", nullable = false, columnDefinition = "varchar(64) not null comment '邮件服务器SMTP地址'")
	private String host;


	/**
	 * 密码
	 */
	@Column(name = "pass", nullable = false, columnDefinition = "varchar(64) not null comment '密码'")
	private String pass;


	/**
	 * 端口
	 */
	@Column(name = "port", nullable = false, columnDefinition = "varchar(64) not null comment '端口'")
	private String port;


	/**
	 * 发件者用户名
	 */
	@Column(name = "user", nullable = false, columnDefinition = "varchar(64) not null comment '发件者用户名'")
	private String user;

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
