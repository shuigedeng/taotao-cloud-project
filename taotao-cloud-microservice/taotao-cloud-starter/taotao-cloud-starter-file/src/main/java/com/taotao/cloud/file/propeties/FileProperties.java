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
 * 文件服务Properties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 09:39
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = UploadFileConstant.BASE_UPLOAD_FILE_PREFIX)
public class FileProperties {

	/**
	 * 是否开启
	 */
	private Boolean enabled = false;

	/**
	 * 类型
	 */
	private String type = UploadFileConstant.DFS_ALIYUN;

}
