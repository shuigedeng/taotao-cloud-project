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
 * nginx文件服务Properties
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 09:39
 */
@RefreshScope
@ConfigurationProperties(prefix = NginxProperties.PREFIX)
public class NginxProperties {

	public static final String PREFIX = "taotao.cloud.oss.nginx";

	/**
	 * nginx上传路径
	 */
	private String uploadPath;
	/**
	 * nginx文件下载路径
	 */
	private String downPath;


	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getDownPath() {
		return downPath;
	}

	public void setDownPath(String downPath) {
		this.downPath = downPath;
	}
}
