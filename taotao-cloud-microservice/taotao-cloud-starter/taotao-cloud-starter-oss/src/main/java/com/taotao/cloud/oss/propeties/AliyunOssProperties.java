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
package com.taotao.cloud.oss.propeties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 阿里云文件服务Properties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 09:39
 */
@RefreshScope
@ConfigurationProperties(prefix = AliyunOssProperties.PREFIX)
public class AliyunOssProperties {

	public static final String PREFIX = "taotao.cloud.oss.aliyun";

	/**
	 * 阿里云绑定的域名
	 */
	private String domain;

	/**
	 * 阿里云EndPoint
	 */
	private String endPoint;

	/**
	 * 阿里云AccessKeyId
	 */
	private String accessKeyId;

	/**
	 * 阿里云AccessKeySecret
	 */
	private String accessKeySecret;

	/**
	 * 阿里云BucketName
	 */
	private String bucketName;

	/**
	 * 阿里云urlPrefix
	 */
	private String urlPrefix;


	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
}
