/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.elasticsearch.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * es httpClient连接池配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/3 06:47
 */
@RefreshScope
@ConfigurationProperties(prefix = RestClientPoolProperties.PREFIX)
public class RestClientPoolProperties {

	public static final String PREFIX = "taotao.cloud.elasticsearch.rest-pool";

	/**
	 * 链接建立超时时间
	 */
	private Integer connectTimeOut = 1000;
	/**
	 * 等待数据超时时间
	 */
	private Integer socketTimeOut = 30000;
	/**
	 * 连接池获取连接的超时时间
	 */
	private Integer connectionRequestTimeOut = 500;
	/**
	 * 最大连接数
	 */
	private Integer maxConnectNum = 30;
	/**
	 * 最大路由连接数
	 */
	private Integer maxConnectPerRoute = 10;

	public Integer getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(Integer connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	public Integer getSocketTimeOut() {
		return socketTimeOut;
	}

	public void setSocketTimeOut(Integer socketTimeOut) {
		this.socketTimeOut = socketTimeOut;
	}

	public Integer getConnectionRequestTimeOut() {
		return connectionRequestTimeOut;
	}

	public void setConnectionRequestTimeOut(Integer connectionRequestTimeOut) {
		this.connectionRequestTimeOut = connectionRequestTimeOut;
	}

	public Integer getMaxConnectNum() {
		return maxConnectNum;
	}

	public void setMaxConnectNum(Integer maxConnectNum) {
		this.maxConnectNum = maxConnectNum;
	}

	public Integer getMaxConnectPerRoute() {
		return maxConnectPerRoute;
	}

	public void setMaxConnectPerRoute(Integer maxConnectPerRoute) {
		this.maxConnectPerRoute = maxConnectPerRoute;
	}
}
