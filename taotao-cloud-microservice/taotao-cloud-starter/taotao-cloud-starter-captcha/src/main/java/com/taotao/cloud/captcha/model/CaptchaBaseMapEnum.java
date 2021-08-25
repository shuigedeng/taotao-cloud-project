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
package com.taotao.cloud.captcha.model;

/**
 * 底图类型枚举
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:46
 */
public enum CaptchaBaseMapEnum {
	ORIGINAL("ORIGINAL", "滑动拼图底图"),
	SLIDING_BLOCK("SLIDING_BLOCK", "滑动拼图滑块底图"),
	PIC_CLICK("PIC_CLICK", "文字点选底图");

	private final String codeValue;
	private final String codeDesc;

	CaptchaBaseMapEnum(String codeValue, String codeDesc) {
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
	public static CaptchaBaseMapEnum parseFromCodeValue(String codeValue) {
		for (CaptchaBaseMapEnum e : CaptchaBaseMapEnum.values()) {
			if (e.codeValue.equals(codeValue)) {
				return e;
			}
		}
		return null;
	}

	//根据codeValue获取描述
	public static String getCodeDescByCodeBalue(String codeValue) {
		CaptchaBaseMapEnum enumItem = parseFromCodeValue(codeValue);
		return enumItem == null ? "" : enumItem.getCodeDesc();
	}

	//验证codeValue是否有效
	public static boolean validateCodeValue(String codeValue) {
		return parseFromCodeValue(codeValue) != null;
	}

	//列出所有值字符串
	public static String getString() {
		StringBuffer buffer = new StringBuffer();
		for (CaptchaBaseMapEnum e : CaptchaBaseMapEnum.values()) {
			buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
		}
		buffer.deleteCharAt(buffer.lastIndexOf(","));
		return buffer.toString().trim();
	}

}
