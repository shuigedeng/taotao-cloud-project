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
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发票子内容
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:19:44
 */
@RecordBuilder
@Schema(description = "发票子内容")
public record ReceiptAddCmd(
        @Schema(description = "发票ID") String receiptId,
        @Schema(description = "商品名称") String goodsName,
        @Schema(description = "规格") String specs,
        @Schema(description = "数量") Integer num,
        @Schema(description = "单价") BigDecimal goodPrice,
        @Schema(description = "小计") BigDecimal subtotal)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;
}
