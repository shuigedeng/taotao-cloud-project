/*
 * Copyright 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.model;


import java.util.Collection;

/**
 * 通知信息
 *
 * @author shuigedeng
 */
public class NoticeInfo {

	/**
	 * 通知内容
	 */
	private NoticeData noticeData;

	/**
	 * 号码列表
	 */
	private Collection<String> phones;

	public NoticeData getNoticeData() {
		return noticeData;
	}

	public void setNoticeData(NoticeData noticeData) {
		this.noticeData = noticeData;
	}

	public Collection<String> getPhones() {
		return phones;
	}

	public void setPhones(Collection<String> phones) {
		this.phones = phones;
	}
}
