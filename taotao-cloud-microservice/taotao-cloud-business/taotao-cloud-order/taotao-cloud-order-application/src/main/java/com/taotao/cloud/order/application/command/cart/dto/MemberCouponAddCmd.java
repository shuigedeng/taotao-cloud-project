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

package com.taotao.cloud.order.application.command.cart.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 用于计算优惠券结算详情
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:16:59
 */
@RecordBuilder
@Schema(description = "用于计算优惠券结算详情")
public record MemberCouponAddCmd(
        @Schema(description = "在整比交易中时： key 为店铺id，value 为每个店铺跨店优惠 结算金额 在购物车中时：key为sku id" + " ，value为每个商品结算时的金额")
                Map<String, BigDecimal> skuDetail,
        @Schema(description = "优惠券详情") InnerMemberCouponDTO memberCoupon)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8276369124551043085L;

    public static class InnerMemberCouponDTO {}
}
