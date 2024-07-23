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

package com.taotao.cloud.order.application.command.order.dto.clientobject;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/** 投诉通信VO */
@RecordBuilder
@Schema(description = "投诉通信VO")
public record OrderComplaintCommunicationCO(
        @Schema(description = "投诉通信") OrderComplaintCommunicationBaseCO orderComplaintCommunicationBase)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -8460949951683122695L;

    // public OrderComplaintCommunicationVO(Long complainId, String content, String owner,
    // 	String ownerName, Long ownerId) {
    // 	super(complainId, content, owner, ownerName, ownerId);
    // }
}
