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

package com.taotao.cloud.message.biz.model.vo;

import com.taotao.cloud.message.api.enums.MessageShowTypeEnum;
import com.taotao.cloud.message.api.enums.RangeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-25 15:46:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息")
public class MessageShowVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标题")
    private String title;

    /**
     * @see MessageShowTypeEnum
     */
    @Schema(description = "消息类型")
    private String type;

    @Schema(description = "消息内容")
    private String content;
    /**
     * @see RangeEnum
     */
    @Schema(description = "发送范围")
    private String messageRange;
}
