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
package com.taotao.cloud.oss.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.propeties.MinioProperties;
import com.taotao.cloud.oss.propeties.OssProperties;
import com.taotao.cloud.oss.service.UploadFileService;
import com.taotao.cloud.oss.service.impl.MinioUploadFileServiceImpl;
import io.minio.MinioClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio自动配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 10:49
 */
@Configuration
@EnableConfigurationProperties({MinioProperties.class,})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "MINIO")
public class MinioOssAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(MinioOssAutoConfiguration.class, StarterName.OSS_STARTER);
	}

	private final MinioProperties properties;

	public MinioOssAutoConfiguration(MinioProperties properties) {
		this.properties = properties;
	}

	@Bean
	public MinioClient minioClient() {
		return MinioClient.builder()
			.endpoint(properties.getUrl())
			.credentials(properties.getAccessKey(), properties.getSecretKey())
			.build();
	}

	@Bean
	public UploadFileService fileUpload(MinioProperties properties, MinioClient minioClient) {
		return new MinioUploadFileServiceImpl(properties, minioClient);
	}
}
