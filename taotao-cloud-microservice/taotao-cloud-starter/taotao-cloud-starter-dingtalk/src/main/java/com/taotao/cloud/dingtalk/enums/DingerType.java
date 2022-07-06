/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.enums;

/**
 * Dinger类型
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:20:51
 */
public enum DingerType {
	DINGTALK("钉钉", "https://oapi.dingtalk.com/robot/send?access_token", true),
	WETALK("企业微信", "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key", true);

	private final String type;
	private final String robotUrl;
	/**
	 * 是否开启
	 */
	private final boolean enabled;

	DingerType(String type, String robotUrl, boolean enabled) {
		this.type = type;
		this.robotUrl = robotUrl;
		this.enabled = enabled;
	}

	public String getType() {
		return type;
	}

	public String getRobotUrl() {
		return robotUrl;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
