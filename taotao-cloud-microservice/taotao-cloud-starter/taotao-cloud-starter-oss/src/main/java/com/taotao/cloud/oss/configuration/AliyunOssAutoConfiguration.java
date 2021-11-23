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
package com.taotao.cloud.oss.configuration;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.taotao.cloud.oss.propeties.AliyunOssProperties;
import com.taotao.cloud.oss.propeties.OssProperties;
import com.taotao.cloud.oss.service.UploadFileService;
import com.taotao.cloud.oss.service.impl.AliossUploadFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 阿里云oss自动配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 10:49
 */
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "ALIYUN")
public class AliyunOssAutoConfiguration {

	private final AliyunOssProperties properties;

	public AliyunOssAutoConfiguration(AliyunOssProperties properties) {
		this.properties = properties;
	}

	@Bean
	public OSS oss() {
		String endpoint = properties.getEndPoint();
		String accessKey = properties.getAccessKeyId();
		String secretKey = properties.getAccessKeySecret();
		return new OSSClientBuilder().build(endpoint, accessKey, secretKey);
	}

	@Bean
	public UploadFileService fileUpload(OSS oss) {
		return new AliossUploadFileServiceImpl(properties, oss);
	}
}
