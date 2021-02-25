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
package com.taotao.cloud.customer.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dengtao
 * @date 2020/11/20 上午9:42
 * @since v1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "机器人客服VO", description = "机器人客服VO")
public class ChatbotVO  implements Serializable {
	private static final long serialVersionUID = 5126530068827085130L;

	@ApiModelProperty(value = "机器人名称")
	private String name;

	@ApiModelProperty(value = "基础url")
	private String baseUrl;

	@ApiModelProperty(value = "首选语言")
	private String primaryLanguage;

	@ApiModelProperty(value = "兜底回复")
	private String fallback;

	@ApiModelProperty(value = "欢迎语")
	private String welcome;

	@ApiModelProperty(value = "渠道类型")
	private String channel;

	@ApiModelProperty(value = "渠道标识")
	private String channelMark;

	@ApiModelProperty(value = "是否开启 0-未开启，1-开启")
	private Boolean enabled;

	@ApiModelProperty(value = "工作模式")
	private Integer workMode;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
