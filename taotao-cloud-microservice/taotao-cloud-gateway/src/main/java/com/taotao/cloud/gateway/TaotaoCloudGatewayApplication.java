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
package com.taotao.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TaotaoCloudGatewayApplication
 *
 * @author dengtao
 * @since 2020/10/10 09:06
 * @version 1.0.0
 */
//@EnableTaoTaoCloudRequestLog
//@EnableTaoTaoCloudRedis
//@EnableTaoTaoCloudOAuth2RedisTokenStore
//@EnableAutoConfiguration(exclude = {ResourceServerTokenRelayAutoConfiguration.class})
// @ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
// 	classes = {ResourceServerTokenRelayAutoConfiguration.class})})
@SpringBootApplication
@EnableDiscoveryClient
public class TaotaoCloudGatewayApplication {

	public static void main(String[] args) {
		/**
		 * 设置nacos客户端日志和快照目录
		 *
		 * @see LocalConfigInfoProcessor
		 * @since 2021/2/2 上午10:48
		 */
		// System.setProperty("JM.LOG.PATH", "");
		// System.setProperty("JM.SNAPSHOT.PATH", "");

		SpringApplication.run(TaotaoCloudGatewayApplication.class, args);
	}
}
