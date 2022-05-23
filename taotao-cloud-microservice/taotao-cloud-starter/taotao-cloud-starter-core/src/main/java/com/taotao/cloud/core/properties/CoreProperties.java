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
package com.taotao.cloud.core.properties;

import com.taotao.cloud.core.enums.EnvironmentEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * CoreProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:43:31
 */
@RefreshScope
@ConfigurationProperties(prefix = CoreProperties.PREFIX)
public class CoreProperties {

	public static final String PREFIX = "taotao.cloud.core";

	public static String SpringApplicationName = "spring.application.name";
	public static String SpringProfilesActive = "spring.profiles.active";

	public static String LoggingFileTotalSize = "logging.file.total-size";
	public static String ContextRestartText = "taotao.cloud.core.context.restart.text";

	/**
	 * 核心环境变量
	 */
	private EnvironmentEnum env;
	/**
	 * 是否开启核心属性配置
	 */
	private boolean enabled = true;
	/**
	 * 是否开启自定义收集器
	 */
	private boolean collectHookEnabled = true;
	/**
	 * 是否开启配置刷新上下文监听
	 */
	private boolean contextRestartEnabled = false;
	/**
	 * 刷新上下文监听等待时间
	 */
	private int contextRestartTimespan = 10;


	public EnvironmentEnum getEnv() {
		return env;
	}

	public void setEnv(EnvironmentEnum env) {
		this.env = env;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getCollectHookEnabled() {
		return collectHookEnabled;
	}

	public void setCollectHookEnabled(boolean collectHookEnabled) {
		this.collectHookEnabled = collectHookEnabled;
	}

	public boolean getContextRestartEnabled() {
		return contextRestartEnabled;
	}

	public void setContextRestartEnabled(boolean contextRestartEnabled) {
		this.contextRestartEnabled = contextRestartEnabled;
	}

	public int getContextRestartTimespan() {
		return contextRestartTimespan;
	}

	public void setContextRestartTimespan(int contextRestartTimespan) {
		this.contextRestartTimespan = contextRestartTimespan;
	}
}
