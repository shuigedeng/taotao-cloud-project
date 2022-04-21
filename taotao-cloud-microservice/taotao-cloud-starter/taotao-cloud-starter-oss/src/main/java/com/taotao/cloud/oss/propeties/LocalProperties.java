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
package com.taotao.cloud.oss.propeties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 本地文件服务Properties
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 09:40
 */
@RefreshScope
@ConfigurationProperties(prefix = LocalProperties.PREFIX)
public class LocalProperties {

	public static final String PREFIX = "taotao.cloud.oss.local";

	public static String sysPath = System.getProperty("user.dir");

	private String endpoint = "http://127.0.0.1:8080";

	private String filePath = sysPath + "/upload";

	private String filDir = "/upload";

	public static String getSysPath() {
		return sysPath;
	}

	public static void setSysPath(String sysPath) {
		LocalProperties.sysPath = sysPath;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilDir() {
		return filDir;
	}

	public void setFilDir(String filDir) {
		this.filDir = filDir;
	}
}
