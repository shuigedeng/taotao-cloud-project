package com.taotao.cloud.health.base;

/**
 * 报警消息
 *
 * @author: chejiangyi
 * @version: 2019-07-24 13:44
 **/
public class Message {

	private EnumWarnType warnType;
	private String title;
	private String content;
	private BsfLevel levelType = BsfLevel.LOW;
	private BsfExceptionType exceptionType = BsfExceptionType.BE;
	private String exceptionCode;
	private String bizScope;

	public Message() {
	}

	public Message(EnumWarnType warnType, String title, String content,
		BsfLevel levelType, BsfExceptionType exceptionType, String exceptionCode,
		String bizScope) {
		this.warnType = warnType;
		this.title = title;
		this.content = content;
		this.levelType = levelType;
		this.exceptionType = exceptionType;
		this.exceptionCode = exceptionCode;
		this.bizScope = bizScope;
	}

	public Message(EnumWarnType warnType, String title, String content) {
		this.warnType = warnType;
		this.title = title;
		this.content = content;
	}

	public Message(String title, String content, BsfLevel enumLevelType) {
		this.warnType = EnumWarnType.ERROR;
		this.title = title;
		this.content = content;
		this.levelType = enumLevelType;
	}

	public Message(String title, String content, BsfExceptionType BsfExceptionType) {
		this.warnType = EnumWarnType.ERROR;
		this.title = title;
		this.content = content;
		this.exceptionType = BsfExceptionType;
	}

	public Message(String title, String content) {
		this.warnType = EnumWarnType.ERROR;
		this.title = title;
		this.content = content;
	}

	public Message(String title, String content, String exceptionCode) {
		this.warnType = EnumWarnType.ERROR;
		this.title = title;
		this.content = content;
		this.exceptionCode = exceptionCode;
	}

	public EnumWarnType getWarnType() {
		return warnType;
	}

	public void setWarnType(EnumWarnType warnType) {
		this.warnType = warnType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BsfLevel getLevelType() {
		return levelType;
	}

	public void setLevelType(BsfLevel levelType) {
		this.levelType = levelType;
	}

	public BsfExceptionType getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(BsfExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getBizScope() {
		return bizScope;
	}

	public void setBizScope(String bizScope) {
		this.bizScope = bizScope;
	}
}
