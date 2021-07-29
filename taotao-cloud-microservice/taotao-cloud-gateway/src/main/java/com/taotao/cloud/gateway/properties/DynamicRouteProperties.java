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
package com.taotao.cloud.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 动态路由配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 11:15
 *
 * Data Id：scg-routes
 * Group：SCG_GATEWAY
 *
 * [{
 *     "id": "consumer-router",
 *     "order": 0,
 *     "predicates": [{
 *         "args": {
 *             "pattern": "/consume/**"
 *         },
 *         "name": "Path"
 *     }],
 *     "uri": "lb://nacos-consumer"
 * },{
 *     "id": "provider-router",
 *     "order": 2,
 *     "predicates": [{
 *         "args": {
 *             "pattern": "/provide/**"
 *         },
 *         "name": "Path"
 *     }],
 *     "uri": "lb://nacos-provider"
 * }]
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = DynamicRouteProperties.PREFIX)
public class DynamicRouteProperties {

	public static final String PREFIX = "taotao.cloud.gateway.dynamic.route";

	/**
	 * 是否开启
	 */
	private Boolean enabled = false;

	/**
	 * 类型
	 */
	private String type = "nacos";

	private String dataId = "";

	private String groupId = "";

}
