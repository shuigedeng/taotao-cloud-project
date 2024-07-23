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

package com.taotao.cloud.order.application.command.aftersale.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商城退款流水
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:16:50
 */
@RecordBuilder
@Schema(description = "商城退款流水")
public record AfterSalePriceDetailDTO(
        @Schema(description = "商品总金额（商品原价）") BigDecimal goodsPrice,
        @Schema(description = "配送费") BigDecimal freightPrice,

        // ============discount price============

        @Schema(description = "支付积分") Integer payPoint,
        @Schema(description = "优惠金额") BigDecimal discountPrice,
        @Schema(description = "优惠券金额") BigDecimal couponPrice,

        // ===========end discount price =============

        // =========distribution==========

        @Schema(description = "单品分销返现支出") BigDecimal distributionCommission,
        @Schema(description = "平台收取交易佣金") BigDecimal platFormCommission,

        // =========end distribution==========

        // ========= platform coupon==========

        @Schema(description = "平台优惠券 使用金额") BigDecimal siteCouponPrice,
        @Schema(description = "站点优惠券佣金比例") BigDecimal siteCouponPoint,
        @Schema(description = "站点优惠券佣金") BigDecimal siteCouponCommission,
        // =========end platform coupon==========

        @Schema(description = "流水金额(入账 出帐金额) = goodsPrice - discountPrice - couponPrice") BigDecimal flowPrice,
        @Schema(description = "最终结算金额 = flowPrice - platFormCommission - distributionCommission") BigDecimal billPrice,
        @Schema(description = "参与的促销活动") List<BasePromotionsDTO> joinPromotion)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;

    public static class BasePromotionsDTO {}
}
