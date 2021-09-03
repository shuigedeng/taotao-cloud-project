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
package com.taotao.cloud.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * IpRegexProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:42:30
 */
@RefreshScope
@ConfigurationProperties(prefix = IpRegexProperties.PREFIX)
public class IpRegexProperties {

	public static final String PREFIX = "taotao.cloud.core.ip";

	/**
	 * include
	 */
	private Include include;
	/**
	 * exclude
	 */
	private Exclude exclude;

	private static class Include {

		private String regex;

		public String getRegex() {
			return regex;
		}

		public void setRegex(String regex) {
			this.regex = regex;
		}
	}


	private static class Exclude {

		private String regex;

		public String getRegex() {
			return regex;
		}

		public void setRegex(String regex) {
			this.regex = regex;
		}
	}

	public Include getInclude() {
		return include;
	}

	public void setInclude(Include include) {
		this.include = include;
	}

	public Exclude getExclude() {
		return exclude;
	}

	public void setExclude(Exclude exclude) {
		this.exclude = exclude;
	}
}
