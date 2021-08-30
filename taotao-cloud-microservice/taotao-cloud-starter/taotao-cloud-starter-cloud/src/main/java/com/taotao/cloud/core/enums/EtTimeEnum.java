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
package com.taotao.cloud.core.enums;

/**
 * EtTimeEnum
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:29
 */
public enum EtTimeEnum {
	S01(1, 1, "延迟1秒"),
	S05(2, 5, "延迟5秒"),
	S10(3, 10, "延迟10秒"),
	S30(4, 30, "延迟30秒"),
	M01(5, 60, "延迟1分钟"),
	M02(6, 60 * 2, "延迟2分钟"),
	M03(7, 60 * 3, "延迟3分钟"),
	M04(8, 60 * 4, "延迟4分钟"),
	M05(9, 60 * 5, "延迟5分钟"),
	M16(10, 60 * 6, "延迟6分钟"),
	M07(11, 60 * 7, "延迟7分钟"),
	M08(12, 60 * 8, "延迟8分钟"),
	M09(13, 60 * 9, "延迟9分钟"),
	M10(14, 60 * 10, "延迟10分钟"),
	M20(15, 60 * 20, "延迟20分钟"),
	M30(16, 60 * 30, "延迟30分钟"),
	H01(17, 60 * 60, "延迟1小时"),
	H02(18, 60 * 60 * 2, "延迟2小时");

	private int code;
	private int second;
	private String message;

	EtTimeEnum(int code, int second, String message) {
		this.code = code;
		this.second = second;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public int getSecond() {
		return second;
	}
}
