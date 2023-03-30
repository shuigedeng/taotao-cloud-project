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

package com.taotao.cloud.report.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 订单统计概述 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderOverviewVO {

    @Schema(description = "UV人次")
    private Long uvNum;

    /** 下单统计 */
    @Schema(description = "下单数量")
    private Long orderNum;

    @Schema(description = "下单人数")
    private Long orderMemberNum;

    @Schema(description = "下单金额")
    private BigDecimal orderAmount;

    /** 付款统计 */
    @Schema(description = "付款订单数量")
    private Long paymentOrderNum;

    @Schema(description = "付款人数")
    private Long paymentsNum;

    @Schema(description = "付款金额")
    private BigDecimal paymentAmount;

    /** 退单统计 */
    @Schema(description = "退单笔数")
    private Long refundOrderNum;

    @Schema(description = "退单金额")
    private BigDecimal refundOrderPrice;

    /** 转换率 */
    @Schema(description = "下单转换率")
    private String orderConversionRate;

    @Schema(description = "付款转换率")
    private String paymentsConversionRate;

    @Schema(description = "整体转换率")
    private String overallConversionRate;

    public Long getUvNum() {
        if (uvNum == null) {
            return 0L;
        }
        return uvNum;
    }

    public Long getOrderNum() {
        if (orderNum == null) {
            return 0L;
        }
        return orderNum;
    }

    public Long getOrderMemberNum() {
        if (orderMemberNum == null) {
            return 0L;
        }
        return orderMemberNum;
    }

    public BigDecimal getOrderAmount() {
        if (orderAmount == null) {
            return BigDecimal.ZERO;
        }
        return orderAmount;
    }

    public Long getPaymentOrderNum() {
        if (paymentOrderNum == null) {
            return 0L;
        }
        return paymentOrderNum;
    }

    public Long getPaymentsNum() {
        if (paymentsNum == null) {
            return 0L;
        }
        return paymentsNum;
    }

    public BigDecimal getPaymentAmount() {
        if (paymentAmount == null) {
            return BigDecimal.ZERO;
        }
        return paymentAmount;
    }

    public Long getRefundOrderNum() {
        if (refundOrderNum == null) {
            return 0L;
        }
        return refundOrderNum;
    }

    public BigDecimal getRefundOrderPrice() {
        if (refundOrderPrice == null) {
            return BigDecimal.ZERO;
        }
        return refundOrderPrice;
    }
}
