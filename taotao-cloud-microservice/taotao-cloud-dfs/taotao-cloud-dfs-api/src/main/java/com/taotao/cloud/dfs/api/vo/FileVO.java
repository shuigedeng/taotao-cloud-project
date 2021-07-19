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
package com.taotao.cloud.dfs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author shuigedeng
 * @since 2020/11/12 17:10
 * @version 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "文件VO", description = "文件VO")
public class FileVO implements Serializable {
	private static final long serialVersionUID = 5126530068827085130L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "业务ID")
	private Long bizId;

	@ApiModelProperty(value = "业务类型")
	private String bizType;

	@ApiModelProperty(value = "数据类型")
	private String dataType;

	@ApiModelProperty(value = "原始文件名")
	private String originalFileName;

	@ApiModelProperty(value = "文件访问链接")
	private String url;

	@ApiModelProperty(value = "文件md5值")
	private String fileMd5;

	@ApiModelProperty(value = "文件上传类型")
	private String contextType;

	@ApiModelProperty(value = "唯一文件名")
	private String filename;

	@ApiModelProperty(value = "后缀(没有.)")
	private String ext;

	@ApiModelProperty(value = "大小")
	private Long size;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
