/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serializable;

/**
 * @author hupeng
 * @date 2018-11-24
 */
@TableName("log")
public class Log extends BaseSuperEntity<Log, Long> implements Serializable {

    @TableId
    private Long id;

    /** 操作用户 */
    private String username;

    @TableField(exist = false)
    private String nickname;

    /** 描述 */
    private String description;

    /** 方法名 */
    private String method;

    private Long uid;

    private Integer type;

    /** 参数 */
    private String params;

    /** 日志类型 */
    private String logType;

    /** 请求ip */
    private String requestIp;

    /** 地址 */
    private String address;

    /** 浏览器  */
    private String browser;

    /** 请求耗时 */
    private Long time;

    /** 异常详细  */
    private byte[] exceptionDetail;

    public Log(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }

	public Log(Long id, String username, String nickname, String description, String method,
		Long uid, Integer type, String params, String logType, String requestIp,
		String address, String browser, Long time, byte[] exceptionDetail) {
		this.id = id;
		this.username = username;
		this.nickname = nickname;
		this.description = description;
		this.method = method;
		this.uid = uid;
		this.type = type;
		this.params = params;
		this.logType = logType;
		this.requestIp = requestIp;
		this.address = address;
		this.browser = browser;
		this.time = time;
		this.exceptionDetail = exceptionDetail;
	}

	@Override
	public Long getId() {

		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

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

	public byte[] getExceptionDetail() {
		return exceptionDetail;
	}

	public void setExceptionDetail(byte[] exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
}
