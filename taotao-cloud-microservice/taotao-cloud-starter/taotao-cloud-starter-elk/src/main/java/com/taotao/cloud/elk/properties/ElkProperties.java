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
package com.taotao.cloud.elk.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * ElkProperties
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/3 16:18
 */
@RefreshScope
@ConfigurationProperties(prefix = ElkProperties.PREFIX)
public class ElkProperties {

	public static final String PREFIX = "taotao.cloud.elk";

	private boolean enabled = false;

	private String appName = "";

	private String springAppName = "";

	private String[] destinations = {"127.0.0.1:4560"};

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSpringAppName() {
		return springAppName;
	}

	public void setSpringAppName(String springAppName) {
		this.springAppName = springAppName;
	}

	public String[] getDestinations() {
		return destinations;
	}

	public void setDestinations(String[] destinations) {
		this.destinations = destinations;
	}
}
