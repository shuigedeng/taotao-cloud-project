package com.taotao.cloud.health.alarm.core.loader.entity;


import java.io.Serializable;

/**
 * 注册实体类
 * <p>
 */
public class RegisterInfo implements Serializable {

    // 报警规则的路径
    private String alarmConfPath;

    // 最多可配置的报警规则类型
    private Integer maxAlarmType;


    // 默认的报警用户
    private String defaultAlarmUsers;


    // 应用名
    private String appName;


    // 报警规则配置信息
    private String emailHost; // 如 smtp.163.com
    private Integer emailPort; // 25
    private String emailUname; // 发送邮件的用户名
    private String emailToken; // 发送邮件的token
    private String emailFrom; // 发送邮件的帐号
    private Boolean emailSsl; // true 表示启用ssl发送

	public String getAlarmConfPath() {
		return alarmConfPath;
	}

	public void setAlarmConfPath(String alarmConfPath) {
		this.alarmConfPath = alarmConfPath;
	}

	public Integer getMaxAlarmType() {
		return maxAlarmType;
	}

	public void setMaxAlarmType(Integer maxAlarmType) {
		this.maxAlarmType = maxAlarmType;
	}

	public String getDefaultAlarmUsers() {
		return defaultAlarmUsers;
	}

	public void setDefaultAlarmUsers(String defaultAlarmUsers) {
		this.defaultAlarmUsers = defaultAlarmUsers;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getEmailHost() {
		return emailHost;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}

	public Integer getEmailPort() {
		return emailPort;
	}

	public void setEmailPort(Integer emailPort) {
		this.emailPort = emailPort;
	}

	public String getEmailUname() {
		return emailUname;
	}

	public void setEmailUname(String emailUname) {
		this.emailUname = emailUname;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public Boolean getEmailSsl() {
		return emailSsl;
	}

	public void setEmailSsl(Boolean emailSsl) {
		this.emailSsl = emailSsl;
	}
}
