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
package com.taotao.cloud.oss.up;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.UploadFileService;
import com.upyun.RestManager;
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
@EnableConfigurationProperties({UpProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "UP")
public class UpConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(UpConfiguration.class, StarterName.OSS_UP_STARTER);
	}

	private final UpProperties properties;

	public UpConfiguration(UpProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public RestManager upYun() {
		// 创建实例
		RestManager upyun = new RestManager(properties.getBucketName(), properties.getUserName(),
			properties.getPassword());
		// 可选属性1，是否开启 debug 模式，默认不开启
		//upyun.setDebug(false);
		// 可选属性2，超时时间，默认 30s
		upyun.setTimeout(30);
		return upyun;
	}

	@Bean
	@ConditionalOnMissingBean
	public UploadFileService fileUpload(RestManager upyun) {
		return new UpUploadFileServiceImpl(upyun, properties);
	}

}
