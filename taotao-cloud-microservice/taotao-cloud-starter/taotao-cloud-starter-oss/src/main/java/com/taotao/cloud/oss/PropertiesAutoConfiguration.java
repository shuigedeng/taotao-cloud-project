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
package com.taotao.cloud.oss;

import com.taotao.cloud.oss.propeties.AliyunOssProperties;
import com.taotao.cloud.oss.propeties.FastdfsProperties;
import com.taotao.cloud.oss.propeties.FileProperties;
import com.taotao.cloud.oss.propeties.FtpProperties;
import com.taotao.cloud.oss.propeties.LocalProperties;
import com.taotao.cloud.oss.propeties.NginxProperties;
import com.taotao.cloud.oss.propeties.QCloudProperties;
import com.taotao.cloud.oss.propeties.QiniuProperties;
import com.taotao.cloud.oss.propeties.UpYunProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * PropertiesAutoConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/7/29 14:18
 */
@EnableConfigurationProperties({
	FileProperties.class,
	AliyunOssProperties.class,
	FastdfsProperties.class,
	LocalProperties.class,
	NginxProperties.class,
	QCloudProperties.class,
	QiniuProperties.class,
	FtpProperties.class,
	UpYunProperties.class
})
public class PropertiesAutoConfiguration {

}
