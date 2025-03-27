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

package com.taotao.cloud.payment.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * 退款日志
 *
 * @since 2021/1/28 09:21
 */
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class RefundLogVO {

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "退单编号")
    private String afterSaleNo;

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "金额")
    private BigDecimal totalAmount;

    @Schema(description = "改笔交易支付金额")
    private BigDecimal payPrice;

    @Schema(description = "是否已退款")
    private Boolean isRefund;

    @Schema(description = "退款方式")
    private String paymentName;

    @Schema(description = "支付第三方付款流水")
    private String paymentReceivableNo;

    @Schema(description = "退款请求流水")
    private String outOrderNo;

    @Schema(description = "第三方退款流水号")
    private String receivableNo;

    @Schema(description = "退款理由")
    private String refundReason;

    @Schema(description = "退款失败原因")
    private String errorMessage;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
