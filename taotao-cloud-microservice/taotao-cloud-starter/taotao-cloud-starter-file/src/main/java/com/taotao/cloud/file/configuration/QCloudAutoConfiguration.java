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
package com.taotao.cloud.file.configuration;

import com.taotao.cloud.file.propeties.FileProperties;
import com.taotao.cloud.file.propeties.QCloudProperties;
import com.taotao.cloud.file.service.UploadFileService;
import com.taotao.cloud.file.service.impl.QCloudUploadFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 10:28
 */
@ConditionalOnProperty(prefix = FileProperties.PREFIX, name = "type", havingValue = "QCLOUD")
public class QCloudAutoConfiguration {

	private final QCloudProperties properties;

	public QCloudAutoConfiguration(QCloudProperties properties) {
		this.properties = properties;
	}


	@Bean
	public UploadFileService fileUpload() {
		return new QCloudUploadFileServiceImpl(properties);
	}
}
