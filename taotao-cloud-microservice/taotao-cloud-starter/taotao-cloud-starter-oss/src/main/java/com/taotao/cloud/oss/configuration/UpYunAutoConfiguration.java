/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

import com.UpYun;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.propeties.OssProperties;
import com.taotao.cloud.oss.propeties.UpYunProperties;
import com.taotao.cloud.oss.service.UploadFileService;
import com.taotao.cloud.oss.service.impl.UpYunUploadFileServiceImpl;
import org.springframework.beans.factory.InitializingBean;
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
@Configuration
@EnableConfigurationProperties({UpYunProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "UPYUN")
public class UpYunAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(UpYunAutoConfiguration.class, StarterName.OSS_STARTER);
	}

	private final UpYunProperties properties;

	public UpYunAutoConfiguration(UpYunProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public UpYun upYun() {
		// 创建实例
		UpYun upyun = new UpYun(properties.getBucketName(), properties.getUserName(),
			properties.getPassword());
		// 可选属性1，是否开启 debug 模式，默认不开启
		upyun.setDebug(false);
		// 可选属性2，超时时间，默认 30s
		upyun.setTimeout(30);
		return upyun;
	}

	@Bean
	public UploadFileService fileUpload(UpYun upyun) {
		return new UpYunUploadFileServiceImpl(upyun, properties);
	}


}
