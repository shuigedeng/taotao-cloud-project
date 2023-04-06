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

package com.taotao.cloud.workflow.biz.common.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/** Minio客户端 */
@EnableConfigurationProperties(MinioConfigurationProperties.class)
@Component
public class MyMinioClient {
    @Autowired
    private MinioConfigurationProperties minioConfigurationProperties;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    @Bean
    public void minioClient() {
        MinioClient minioClient = null;
        if (StringUtil.isNotEmpty(minioConfigurationProperties.getAccessKey())) {
            minioClient = MinioClient.builder()
                    .endpoint(minioConfigurationProperties.getEndpoint())
                    .credentials(
                            minioConfigurationProperties.getAccessKey(), minioConfigurationProperties.getSecretKey())
                    .build();
            // 将OSS对象手动注入到容器中
            defaultListableBeanFactory.registerSingleton("minioClient", minioClient);
        } else {
            minioClient = MinioClient.builder()
                    .endpoint("yourEndpoint")
                    .credentials("yourAccessKey", "yourSecretKey")
                    .build();
            defaultListableBeanFactory.registerSingleton("minioClient", minioClient);
        }
    }
}
