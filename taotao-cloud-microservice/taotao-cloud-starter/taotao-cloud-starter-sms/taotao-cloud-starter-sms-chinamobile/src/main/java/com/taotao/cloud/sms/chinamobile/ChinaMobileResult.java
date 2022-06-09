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
package com.taotao.cloud.sms.chinamobile;

/**
 * 响应结果
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:50:40
 */
public class ChinaMobileResult {

	/**
	 * 响应状态
	 */
	public static final String SUCCESS_RSPCOD = "success";

	/**
	 * 消息批次号，由云MAS平台生成，用于关联短信发送请求与状态报告，注：若数据验证不通过，该参数值为空
	 */
	private String msgGroup;

	/**
	 * 响应状态
	 */
	private String rspcod;

	/**
	 * 是否成功
	 */
	private boolean success;

	public String getMsgGroup() {
		return msgGroup;
	}

	public void setMsgGroup(String msgGroup) {
		this.msgGroup = msgGroup;
	}

	public String getRspcod() {
		return rspcod;
	}

	public void setRspcod(String rspcod) {
		this.rspcod = rspcod;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
