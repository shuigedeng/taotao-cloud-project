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

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.taotao.cloud.oss.propeties.FileProperties;
import com.taotao.cloud.oss.propeties.QiniuProperties;
import com.taotao.cloud.oss.service.UploadFileService;
import com.taotao.cloud.oss.service.impl.QiniuUploadFileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 10:28
 */
@ConditionalOnProperty(prefix = FileProperties.PREFIX, name = "type", havingValue = "QINIU")
public class QiniuAutoConfiguration {

	private final QiniuProperties properties;

	public QiniuAutoConfiguration(QiniuProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public Zone zone() {
		Zone zone;
		switch (properties.getZone()) {
			case "zone0":
				zone = Zone.zone0();
				break;
			case "zone1":
				zone = Zone.zone1();
				break;
			case "zone2":
				zone = Zone.zone2();
				break;
			default:
				zone = Zone.autoZone();
				break;
		}

		return zone;
	}

	/**
	 * 华南机房
	 */
	@Bean
	public com.qiniu.storage.Configuration config(@Autowired Zone zone) {
		return new com.qiniu.storage.Configuration(zone);
	}

	/**
	 * 构建一个七牛上传工具实例
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/10/26 11:36
	 */
	@Bean
	public UploadManager uploadManager(@Autowired com.qiniu.storage.Configuration config) {
		return new UploadManager(config);
	}

	/**
	 * 认证信息实例
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/10/26 11:36
	 */
	@Bean
	public Auth auth() {
		return Auth.create(properties.getAccessKey(), properties.getSecretKey());
	}

	/**
	 * 构建七牛空间管理实例
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/10/26 11:36
	 */
	@Bean
	public BucketManager bucketManager(@Autowired com.qiniu.storage.Configuration config,
		@Autowired Auth auth) {
		return new BucketManager(auth, config);
	}

	@Bean
	public UploadFileService fileUpload(UploadManager uploadManager, BucketManager bucketManager,
		Auth auth) {
		return new QiniuUploadFileServiceImpl(uploadManager, bucketManager, auth, properties);
	}

}
