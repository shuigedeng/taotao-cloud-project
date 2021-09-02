/*
 * Copyright ©2015-2021 Jaemon. All Rights Reserved.
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
package com.taotao.cloud.dingtalk.session;


import com.taotao.cloud.dingtalk.model.DingerRobot;
import com.taotao.cloud.dingtalk.properties.DingerProperties;

/**
 * Configuration
 *
 * @author Jaemon
 * @version 1.2
 */
public class SessionConfiguration {

	protected DingerProperties dingerProperties;
	protected DingerRobot dingerRobot;

	private SessionConfiguration(DingerProperties dingerProperties, DingerRobot dingerRobot) {
		this.dingerProperties = dingerProperties;
		this.dingerRobot = dingerRobot;
	}

	public static SessionConfiguration of(DingerProperties dingerProperties, DingerRobot dingerRobot) {
		return new SessionConfiguration(dingerProperties, dingerRobot);
	}

	public DingerProperties getDingerProperties() {
		return dingerProperties;
	}

	public DingerRobot getDingerRobot() {
		return dingerRobot;
	}
}
