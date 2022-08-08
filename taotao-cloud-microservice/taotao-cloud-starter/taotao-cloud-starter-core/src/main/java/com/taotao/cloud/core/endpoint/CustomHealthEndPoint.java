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
package com.taotao.cloud.core.endpoint;

import com.taotao.cloud.core.endpoint.indicator.CustomHealthIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.health.Health;

/**
 * CustomEndPoint
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:13:40
 */
@WebEndpoint(id = "customHealth")
public class CustomHealthEndPoint {

	@Autowired
	private CustomHealthIndicator customHealthIndicator;

	@ReadOperation
	public Health health() {
		return customHealthIndicator.getHealth(true);
	}
}
