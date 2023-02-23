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
package com.taotao.cloud.gateway;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TaoTaoCloudGatewayApplication
 *
 * <p>http://127.0.0.1:33333/swagger-ui.html</p>
 * <p>http://127.0.0.1:33333/doc.html</p>
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/10 09:06
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TaoTaoCloudGatewayApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-gateway-springcloud");

		try {
			SpringApplication.run(TaoTaoCloudGatewayApplication.class, args);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
