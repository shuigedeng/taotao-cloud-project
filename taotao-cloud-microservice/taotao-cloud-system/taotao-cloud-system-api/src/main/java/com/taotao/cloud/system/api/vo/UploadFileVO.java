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
package com.taotao.cloud.system.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * @author shuigedeng
 * @since 2020/11/12 17:10
 * @version 1.0.0
 */
@Schema(name = "UploadFileVO", description = "上传文件VO")
public class UploadFileVO implements Serializable {
	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "文件路径")
	private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
