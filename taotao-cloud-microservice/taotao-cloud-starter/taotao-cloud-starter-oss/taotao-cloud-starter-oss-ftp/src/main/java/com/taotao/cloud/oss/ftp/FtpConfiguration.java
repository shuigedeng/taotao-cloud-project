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
package com.taotao.cloud.oss.ftp;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.UploadFileService;
import com.taotao.cloud.oss.common.util.FtpClientUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 10:28
 */
@AutoConfiguration
@EnableConfigurationProperties({FtpProperties.class,})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "FTP")
public class FtpConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(FtpConfiguration.class, StarterName.OSS_STARTER);
	}
	private final FtpProperties properties;

	public FtpConfiguration(FtpProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public FtpClientUtil ftpClient() {
		return new FtpClientUtil(properties.getHost(),
			properties.getPort(),
			properties.getUsername(),
			properties.getPassword(),
			properties.getRemoteDicrory());
	}

	@Bean
	@ConditionalOnMissingBean
	public UploadFileService fileUpload(FtpClientUtil ftpClientUtil) {
		return new FtpUploadFileServiceImpl(ftpClientUtil);
	}


}
