/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.captcha.model;


/**
 * CaptchaTypeEnum 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:18
 */
public enum CaptchaTypeEnum {
	/**
	 * 滑块拼图.
	 */
	BLOCKPUZZLE("blockPuzzle", "滑块拼图"),
	/**
	 * 文字点选.
	 */
	CLICKWORD("clickWord", "文字点选"),
	/**
	 * 默认.
	 */
	DEFAULT("default", "默认");

	private final String codeValue;
	private final String codeDesc;

	CaptchaTypeEnum(String codeValue, String codeDesc) {
		this.codeValue = codeValue;
		this.codeDesc = codeDesc;
	}

	public String getCodeValue() {
		return this.codeValue;
	}

	public String getCodeDesc() {
		return this.codeDesc;
	}

	//根据codeValue获取枚举
	public static CaptchaTypeEnum parseFromCodeValue(String codeValue) {
		for (CaptchaTypeEnum e : CaptchaTypeEnum.values()) {
			if (e.codeValue.equals(codeValue)) {
				return e;
			}
		}
		return null;
	}

	//根据codeValue获取描述
	public static String getCodeDescByCodeBalue(String codeValue) {
		CaptchaTypeEnum enumItem = parseFromCodeValue(codeValue);
		return enumItem == null ? "" : enumItem.getCodeDesc();
	}

	//验证codeValue是否有效
	public static boolean validateCodeValue(String codeValue) {
		return parseFromCodeValue(codeValue) != null;
	}

	//列出所有值字符串
	public static String getString() {
		StringBuffer buffer = new StringBuffer();
		for (CaptchaTypeEnum e : CaptchaTypeEnum.values()) {
			buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
		}
		buffer.deleteCharAt(buffer.lastIndexOf(","));
		return buffer.toString().trim();
	}

}
