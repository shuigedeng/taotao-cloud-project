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
package com.taotao.cloud.sms.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证码内存储存配置
 *
 * @author shuigedeng
 */
@RefreshScope
@ConfigurationProperties(prefix = VerificationCodeMemoryRepositoryProperties.PREFIX)
public class VerificationCodeMemoryRepositoryProperties {

	public static final String PREFIX = "taotao.cloud.sms.verification-code.repository.memory";

	/**
	 * 默认gc频率，单位秒
	 */
	public static final long DEFAULT_GC_FREQUENCY = 300L;

	/**
	 * gc频率，单位秒
	 */
	private long gcFrequency = DEFAULT_GC_FREQUENCY;

	public long getGcFrequency() {
		return gcFrequency;
	}

	public void setGcFrequency(long gcFrequency) {
		this.gcFrequency = gcFrequency;
	}
}
