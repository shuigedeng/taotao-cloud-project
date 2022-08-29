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
package com.taotao.cloud.oss.ali;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.UploadFileService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 阿里云oss自动配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 10:49
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({AliyunOssProperties.class,})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "ALIYUN")
public class AliyunOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(AliyunOssConfiguration.class, StarterName.OSS_STARTER);
	}

	private final AliyunOssProperties properties;

	public AliyunOssConfiguration(AliyunOssProperties properties) {
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
	@ConditionalOnMissingBean
	public UploadFileService fileUpload(OSS oss) {
		return new AliyunOssUploadFileServiceImpl(properties, oss);
	}
}
