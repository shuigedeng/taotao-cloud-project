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
package com.taotao.cloud.file.propeties;

import com.taotao.cloud.file.constant.UploadFileConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 腾讯云服务Properties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 09:39
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = UploadFileConstant.BASE_UPLOAD_FILE_PREFIX
	+ UploadFileConstant.JOINER + UploadFileConstant.DFS_QCLOUD)
public class QCloudProperties {

	/**
	 * 腾讯云绑定的域名
	 */
	private String domain;
	/**
	 * 腾讯云AppId
	 */
	private Integer appId;
	/**
	 * 腾讯云SecretId
	 */
	private String secretId;
	/**
	 * 腾讯云SecretKey
	 */
	private String secretKey;
	/**
	 * 腾讯云BucketName
	 */
	private String bucketName;
	/**
	 * 腾讯云COS所属地区
	 */
	private String region;
}
