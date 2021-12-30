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
package com.taotao.cloud.oauth2.biz;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * TaoTaoCloudOauth2ServerApplication
 * 抑制java9 module 报错
 * --add-opens java.base/java.lang=ALL-UNNAMED
 * --add-opens java.base/java.lang.reflect=ALL-UNNAMED
 * --add-opens java.base/java.util=ALL-UNNAMED
 * --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
 * --add-opens java.base/java.math=ALL-UNNAMED
 * --add-opens java.desktop/sun.awt=ALL-UNNAMED
 * --add-exports java.desktop/sun.awt=ALL-UNNAMED
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 15:13
 */
@EnableJpaRepositories(basePackages = "com.taotao.cloud.oauth2.biz.repository")
@EnableFeignClients(basePackages = {"com.taotao.cloud.*.api.feign"})
@EnableEncryptableProperties
@EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudOauth2BizApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudOauth2BizApplication.class, args);
	}
}
