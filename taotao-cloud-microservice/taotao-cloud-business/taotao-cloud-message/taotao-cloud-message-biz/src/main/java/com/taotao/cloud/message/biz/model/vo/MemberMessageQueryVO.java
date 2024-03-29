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

import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 会员接收消息查询vo */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员接收消息查询vo")
public class MemberMessageQueryVO {

    private static final long serialVersionUID = 1L;

    /**
     * @see MessageStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    @Schema(description = "消息id")
    private String messageId;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "会员id")
    private String memberId;
}
