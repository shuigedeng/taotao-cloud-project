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
import java.math.BigDecimal;

/** 订单价格详情 */
@RecordBuilder
@Schema(description = "订单价格详情")
public record PriceDetailCO(
        @Schema(description = "商品原价") BigDecimal originalPrice,
        @Schema(description = "配送费") BigDecimal freight,
        @Schema(description = "优惠金额") BigDecimal discountPrice,
        @Schema(description = "支付积分") Long payPoint,
        @Schema(description = "最终成交金额") BigDecimal finalePrice)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -960537582096338500L;

    /**
     * 初始化默认值
     *
     * @param dto dto
     * @return {@link PriceDetailCO }
     * @since 2022-05-30 13:58:30
     */
    public static PriceDetailCO priceDetailVO(PriceDetailDTO dto) {
        return PriceDetailVOBuilder.builder()
                .freight(dto.freightPrice())
                .finalePrice(dto.flowPrice())
                .discountPrice(dto.discountPrice())
                .payPoint(dto.payPoint())
                .originalPrice(dto.goodsPrice())
                .build();
    }
}
