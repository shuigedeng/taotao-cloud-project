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

package com.taotao.cloud.message.biz.model.dto;

import com.taotao.cloud.message.api.enums.NoticeMessageNodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 站内信消息
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 14:59:16
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeMessageDTO {

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "消息节点")
    private NoticeMessageNodeEnum noticeMessageNodeEnum;

    @Schema(description = "消息参数")
    private Map<String, String> parameter;
}
