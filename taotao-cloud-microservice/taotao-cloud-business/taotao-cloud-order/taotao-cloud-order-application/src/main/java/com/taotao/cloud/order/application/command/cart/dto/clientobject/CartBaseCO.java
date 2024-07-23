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

package com.taotao.cloud.order.application.command.cart.dto.clientobject;

import com.taotao.cloud.order.sys.model.vo.cart.PriceDetailVOBuilder;
import com.taotao.cloud.order.application.model.dto.order.PriceDetailDTO;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/** 购物车基础 */
@RecordBuilder
@Schema(description = "购物车基础")
public record CartBaseCO(
        @Schema(description = "卖家id") String storeId,
        @Schema(description = "卖家姓名") String storeName,
        @Schema(description = "此商品价格流水计算") PriceDetailDTO priceDetailDTO,
        @Schema(description = "此商品价格展示") PriceDetailCO priceDetailCO)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -5172752506920017597L;

    public PriceDetailCO getPriceDetailVO() {
        if (this.priceDetailDTO != null) {
            return PriceDetailCO.priceDetailVO(priceDetailDTO);
        }
        return PriceDetailVOBuilder.builder().build();
    }
}
