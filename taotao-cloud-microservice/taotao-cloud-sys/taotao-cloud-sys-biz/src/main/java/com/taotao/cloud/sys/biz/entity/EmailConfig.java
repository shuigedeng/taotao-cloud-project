/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;

@TableName("email_config")
public class EmailConfig extends SuperEntity<EmailConfig, Long> implements Serializable {

	/** ID */
	@TableId
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Column(name = "id")
	private Long id;


	/** 收件人 */
	// @Column(name = "from_user")
	private String fromUser;


	/** 邮件服务器SMTP地址 */
	// @Column(name = "host")
	private String host;


	/** 密码 */
	// @Column(name = "pass")
	private String pass;


	/** 端口 */
	// @Column(name = "port")
	private String port;


	/** 发件者用户名 */
	// @Column(name = "user")
	private String user;


	public void copy(EmailConfig source) {
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
