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

package com.taotao.cloud.customer.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 上午9:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "机器人客服VO", description = "机器人客服VO")
public class ChatbotVO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

    @Schema(description = "机器人名称")
    private String name;

    @Schema(description = "基础url")
    private String baseUrl;

    @Schema(description = "首选语言")
    private String primaryLanguage;

    @Schema(description = "兜底回复")
    private String fallback;

    @Schema(description = "欢迎语")
    private String welcome;

    @Schema(description = "渠道类型")
    private String channel;

    @Schema(description = "渠道标识")
    private String channelMark;

    @Schema(description = "是否开启 0-未开启，1-开启")
    private Boolean enabled;

    @Schema(description = "工作模式")
    private Integer workMode;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后修改时间")
    private LocalDateTime lastModifiedTime;
}
