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

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.propeties.OssProperties;
import com.taotao.cloud.oss.propeties.QiniuProperties;
import com.taotao.cloud.oss.service.UploadFileService;
import com.taotao.cloud.oss.service.impl.QiniuUploadFileServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 10:28
 */
@AutoConfiguration
@EnableConfigurationProperties({QiniuProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "QINIU")
public class QiniuAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(QiniuAutoConfiguration.class, StarterName.OSS_STARTER);
	}

	private final QiniuProperties properties;

	public QiniuAutoConfiguration(QiniuProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public Region zone() {
		return switch (properties.getZone()) {
			case "z0" -> Region.region0();
			case "z1" -> Region.region1();
			case "z2" -> Region.region2();
			default -> Region.autoRegion();
		};
	}

	/**
	 * 华南机房
	 */
	@Bean
	public com.qiniu.storage.Configuration config(@Autowired Region region) {
		return new com.qiniu.storage.Configuration(region);
	}

	/**
	 * 构建一个七牛上传工具实例
	 *
	 * @author shuigedeng
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
	 * @version 2022.03
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
