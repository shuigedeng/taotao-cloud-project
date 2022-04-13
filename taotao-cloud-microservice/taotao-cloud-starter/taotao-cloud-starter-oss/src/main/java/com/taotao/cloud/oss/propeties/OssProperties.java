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
 * 文件服务Properties
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 09:39
 */
@RefreshScope
@ConfigurationProperties(prefix = OssProperties.PREFIX)
public class OssProperties {

	public static final String PREFIX = "taotao.cloud.oss";

	/**
	 * 是否开启
	 */
	private Boolean enabled = false;

	/**
	 * 存储类型
	 */
	private DFSTypeEnum type = DFSTypeEnum.ALIYUN;

	/**
	 * 存储类型
	 *
	 * @author shuigedeng
	 * @version 2022.03
	 * @since 2020/4/30 10:25
	 */
	public enum DFSTypeEnum {
		/**
		 * 阿里云存储
		 */
		ALIYUN,
		/**
		 * 腾讯云存储
		 */
		QCLOUD,
		/**
		 * 七牛云存储
		 */
		QINIU,
		/**
		 * fastdfs存储
		 */
		FASTDFS,
		/**
		 * nginx存储
		 */
		NGINX,
		/**
		 * 本地存储
		 */
		LOCAL,
		/**
		 * ftp存储
		 */
		FTP,
		/**
		 * 又拍云存储
		 */
		UPYUN,
		/**
		 * minio存储
		 */
		MINIO;


	}


	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public DFSTypeEnum getType() {
		return type;
	}

	public void setType(DFSTypeEnum type) {
		this.type = type;
	}
}
