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
package com.taotao.cloud.sms.channel.netease;


/**
 * 响应结果
 *
 * @author shuigedeng
 */
public class NeteaseCloudResult {

	/**
	 * 成功代码
	 */
	public static final Integer SUCCESS_CODE = 200;

	/**
	 * 请求返回的结果码。
	 */
	private int code;

	/**
	 * 请求返回的结果码描述。
	 */
	private String msg;

	private Long obj;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getObj() {
		return obj;
	}

	public void setObj(Long obj) {
		this.obj = obj;
	}
}
