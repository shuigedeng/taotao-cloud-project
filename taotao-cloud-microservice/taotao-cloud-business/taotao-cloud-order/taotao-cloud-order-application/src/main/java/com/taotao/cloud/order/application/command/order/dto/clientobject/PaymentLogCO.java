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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.boot.common.enums.ClientTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 订单支付日志：实际为订单部分字段提取过来的一个vo */
@RecordBuilder
@Schema(description = "订单支付日志：实际为订单部分字段提取过来的一个vo")
public record PaymentLogCO(
        @Schema(description = "订单编号") String sn,
        @Schema(description = "交易编号 关联Trade") String tradeSn,
        @Schema(description = "店铺ID") Long storeId,
        @Schema(description = "店铺名称") String storeName,
        @Schema(description = "会员ID") Long memberId,
        @Schema(description = "用户名") String memberName,

        /**
         * @see PayStatusEnum
         */
        @Schema(description = "付款状态") String payStatus,
        @Schema(description = "第三方付款流水号") String receivableNo,
        @Schema(description = "支付方式") String paymentMethod,
        @Schema(description = "支付时间") @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime paymentTime,
        @Schema(description = "总价格") BigDecimal flowPrice,
        @Schema(description = "支付方式返回的交易号") String payOrderNo,

        /**
         * @see ClientTypeEnum
         */
        @Schema(description = "订单来源") String clientType,

        /**
         * @see OrderTypeEnum
         */
        @Schema(description = "订单类型") String orderType)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -6293102172184734928L;
}
