/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.health.model;

import com.taotao.cloud.core.enums.ExceptionTypeEnum;
import com.taotao.cloud.health.enums.WarnLevelEnum;
import com.taotao.cloud.health.enums.WarnTypeEnum;

/**
 * 报警消息
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 16:29:48
 */
public class Message {

	private String title;
	private String content;
	private String exceptionCode;
	private String bizScope;
	private WarnTypeEnum warnType;
	private WarnLevelEnum warnLevelEnumType = WarnLevelEnum.LOW;
	private ExceptionTypeEnum exceptionType = ExceptionTypeEnum.BE;

	public Message() {
	}

	public Message(WarnTypeEnum warnType, String title, String content,
		WarnLevelEnum warnLevelEnumType, ExceptionTypeEnum exceptionType, String exceptionCode,
		String bizScope) {
		this.warnType = warnType;
		this.title = title;
		this.content = content;
		this.warnLevelEnumType = warnLevelEnumType;
		this.exceptionType = exceptionType;
		this.exceptionCode = exceptionCode;
		this.bizScope = bizScope;
	}

	public Message(WarnTypeEnum warnType, String title, String content) {
		this.warnType = warnType;
		this.title = title;
		this.content = content;
	}

	public Message(String title, String content, WarnLevelEnum enumWarnLevelEnumType) {
		this.warnType = WarnTypeEnum.ERROR;
		this.title = title;
		this.content = content;
		this.warnLevelEnumType = enumWarnLevelEnumType;
	}

	public Message(String title, String content, ExceptionTypeEnum ExceptionType) {
		this.warnType = WarnTypeEnum.ERROR;
		this.title = title;
		this.content = content;
		this.exceptionType = ExceptionType;
	}

	public Message(String title, String content) {
		this.warnType = WarnTypeEnum.ERROR;
		this.title = title;
		this.content = content;
	}

	public Message(String title, String content, String exceptionCode) {
		this.warnType = WarnTypeEnum.ERROR;
		this.title = title;
		this.content = content;
		this.exceptionCode = exceptionCode;
	}

	public WarnTypeEnum getWarnType() {
		return warnType;
	}

	public void setWarnType(WarnTypeEnum warnType) {
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

	public WarnLevelEnum getLevelType() {
		return warnLevelEnumType;
	}

	public void setLevelType(WarnLevelEnum warnLevelEnumType) {
		this.warnLevelEnumType = warnLevelEnumType;
	}

	public ExceptionTypeEnum getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(ExceptionTypeEnum exceptionType) {
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
