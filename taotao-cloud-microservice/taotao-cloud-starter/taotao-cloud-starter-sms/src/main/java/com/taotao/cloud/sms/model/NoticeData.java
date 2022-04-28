/*
 * Copyright (c) 2018-2022 the original author or authors.
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


import java.util.Map;

/**
 * 通知内容
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:48:57
 */
public class NoticeData {

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 参数列表
	 */
	private Map<String, String> params;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
