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
package com.taotao.cloud.oss.common.propeties;

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
	private DFSTypeEnum type;

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
		 * AWS存储
		 */
		AWS,
		/**
		 * 百度存储
		 */
		BAIDU,
		/**
		 * fastdfs存储
		 */
		FASTDFS,
		/**
		 * ftp存储
		 */
		FTP,
		/**
		 * 华为存储
		 */
		HUAWEI,
		/**
		 * 京东存储
		 */
		JD,
		/**
		 * jdbc存储
		 */
		JDBC,
		/**
		 * minio存储
		 */
		MINIO,
		/**
		 * nginx存储
		 */
		NGINX,
		/**
		 * 平安存储
		 */
		PINGAN,
		/**
		 * 七牛储
		 */
		QINIU,
		/**
		 * 青云存储
		 */
		QINGYUN,
		/**
		 * sftp存储
		 */
		SFTP,
		/**
		 * 金山存储
		 */
		JINSHAN,
		/**
		 * 腾讯云存储
		 */
		TENCENT,
		/**
		 * ucloud存储
		 */
		UCLOUD,
		/**
		 * 又拍存储
		 */
		UP,
		/**
		 * 网易存储
		 */
		WANGYI,
		/**
		 * 本地存储
		 */
		LOCAL;
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
