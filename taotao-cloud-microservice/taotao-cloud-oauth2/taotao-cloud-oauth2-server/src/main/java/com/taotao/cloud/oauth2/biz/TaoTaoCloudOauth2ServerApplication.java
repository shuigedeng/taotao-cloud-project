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

import com.taotao.cloud.feign.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.feign.annotation.EnableTaoTaoCloudLoadbalancer;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TaoTaoCloudOauth2ServerApplication
 *
 * @author shuigedeng
 * @since 2020/4/29 15:13
 * @version 1.0.0
 */
@EnableTaoTaoCloudLoadbalancer
@EnableTaoTaoCloudFeign
@EnableTaoTaoCloudP6spy
@EnableTaoTaoCloudSentinel
@SpringBootApplication
@EnableDiscoveryClient
public class TaoTaoCloudOauth2ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaoTaoCloudOauth2ServerApplication.class, args);
    }
}
