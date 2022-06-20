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
package com.taotao.cloud.sms.common.properties;

import com.taotao.cloud.sms.common.enums.SmsType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * SmsProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:54:47
 */
@RefreshScope
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {

	public static final String PREFIX = "taotao.cloud.sms";

	private boolean enabled = false;

	/**
	 * 手机号码正则规则
	 */
	private String reg;

	/**
	 * 类型
	 */
	private SmsType type;

	/**
	 * 负载均衡类型
	 * <p>
	 * 可选值: Random、RoundRobin、WeightRandom、WeightRoundRobin， 默认: Random
	 */
	private String loadBalancerType = "Random";

	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	public String getLoadBalancerType() {
		return loadBalancerType;
	}

	public void setLoadBalancerType(String loadBalancerType) {
		this.loadBalancerType = loadBalancerType;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public SmsType getType() {
		return type;
	}

	public void setType(SmsType type) {
		this.type = type;
	}
}
