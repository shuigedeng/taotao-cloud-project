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

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Fastdfs文件服务Properties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 09:40
 */
@RefreshScope
@ConfigurationProperties(prefix = FastdfsProperties.PREFIX)
public class FastdfsProperties {

	public static final String PREFIX = "taotao.cloud.file.fastdfs";

	/**
	 * 读取时间
	 */
	private int soTimeout = 1000;
	/**
	 * 连接超时时间
	 */
	private int connectTimeout = 1000;
	/**
	 * Tracker 地址列表
	 */
	private List<String> trackerList = new ArrayList<>();

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public List<String> getTrackerList() {
		return trackerList;
	}

	public void setTrackerList(List<String> trackerList) {
		this.trackerList = trackerList;
	}
}
