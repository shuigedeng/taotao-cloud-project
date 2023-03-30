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

package com.taotao.cloud.payment.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 退款日志
 *
 * @since 2021/1/28 09:21
 */
@Data
@TableName("tt_refund_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "退款日志")
public class RefundLog extends BaseIdEntity {

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

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;
}
