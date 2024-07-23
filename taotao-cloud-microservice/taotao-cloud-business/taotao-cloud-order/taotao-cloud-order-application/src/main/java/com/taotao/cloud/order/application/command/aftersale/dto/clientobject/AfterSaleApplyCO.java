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

package com.taotao.cloud.order.application.command.aftersale.dto.clientobject;

import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/** 售后申请VO */
@RecordBuilder
@Schema(description = "售后申请VO")
public record AfterSaleApplyCO(
        @Schema(description = "申请退款金额单价") BigDecimal applyRefundPrice,
        @Schema(description = "可申请数量") Integer num,
        @Schema(description = "订单子项编号") String orderItemSn,
        @Schema(description = "商品ID") Long goodsId,
        @Schema(description = "货品ID") Long skuId,
        @Schema(description = "商品名称") String goodsName,
        @Schema(description = "商品图片") String image,
        @Schema(description = "商品价格") BigDecimal goodsPrice,

        /**
         * @see AfterSaleRefundWayEnum
         */
        @Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE") String refundWay,
        @Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,MEMBERWALLET,BANKTRANSFER")
                String accountType,
        @Schema(description = "是否支持退货") Boolean returnGoods,
        @Schema(description = "是否支持退款") Boolean returnMoney,
        @Schema(description = "会员ID") Long memberId)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;
}
