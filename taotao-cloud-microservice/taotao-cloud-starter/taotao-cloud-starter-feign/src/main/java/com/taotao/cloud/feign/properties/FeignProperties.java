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
package com.taotao.cloud.feign.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;

/**
 * RestTemplate 配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2017/11/17
 */
@RefreshScope
@ConfigurationProperties(prefix = FeignProperties.PREFIX)
public class FeignProperties {

	public static final String PREFIX = "taotao.cloud.feign";

	private boolean enabled = false;

	private boolean http = false;

	private boolean okhttp = true;

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getHttp() {
		return http;
	}

	public void setHttp(boolean http) {
		this.http = http;
	}

	public boolean getOkhttp() {
		return okhttp;
	}

	public void setOkhttp(boolean okhttp) {
		this.okhttp = okhttp;
	}


}
