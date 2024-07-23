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

import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 售后dto
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:16:46
 */
@RecordBuilder
@Schema(description = "售后dto")
public record AfterSaleAddCmd(
        @Schema(description = "订单SN") String orderItemSn,
        @Schema(description = "商品ID") String goodsId,
        @Schema(description = "货品ID") String skuId,
        @Schema(description = "申请退款金额") BigDecimal applyRefundPrice,
        @Schema(description = "申请数量") Integer num,
        @Schema(description = "申请原因") String reason,
        @Schema(description = "问题描述") String problemDesc,
        @Schema(description = "售后图片") String images,

        /**
         * @see AfterSaleTypeEnum
         */
        @Schema(description = "售后类型", allowableValues = "RETURN_GOODS,EXCHANGE_GOODS,RETURN_MONEY") String serviceType,

        /**
         * @see AfterSaleRefundWayEnum
         */
        @Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE") String refundWay,
        @Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER") String accountType,
        @Schema(description = "银行开户行") String bankDepositName,
        @Schema(description = "银行开户名") String bankAccountName,
        @Schema(description = "银行卡号") String bankAccountNumber)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;
}
