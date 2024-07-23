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

package com.taotao.cloud.order.application.command.order.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/** 交易投诉DTO */
@RecordBuilder
@Schema(description = "交易投诉DTO")
public record OrderComplaintAddCmd(
        @NotBlank @Schema(description = "投诉主题") String complainTopic,
        @NotBlank @Schema(description = "投诉内容") String content,
        @Schema(description = "投诉凭证图片") String images,
        @NotBlank @Schema(description = "订单号") String orderSn,
        @NotBlank @Schema(description = "商品id") String goodsId,
        @NotBlank @Schema(description = "sku主键") String skuId)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;
}
