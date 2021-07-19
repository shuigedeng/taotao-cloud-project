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
package com.taotao.cloud.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TaoTaoCloudWebApp
 *
 * nohup java -jar /root/taotao-cloud/taotao-cloud-portal-1.0.jar >/root/taotao-cloud/logs/portal.log 2>&1 &
 *
 * @author shuigedeng
 * @since 2021/1/18 下午4:54
 * @version 1.0.0
 */
// @EnableDiscoveryClient
// @EnableFeignClients
@SpringBootApplication
public class TaoTaoCloudPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudPortalApplication.class, args);
	}
}
