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

package com.taotao.cloud.workflow.biz.common.flowable.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结算单传输对象
 *
 * @since 2020/11/17 4:26 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "结算单传输对象")
public class BillDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4441580387361184989L;

    @Schema(description = "结算周期内订单付款总金额")
    private BigDecimal orderPrice;

    @Schema(description = "退单金额")
    private BigDecimal refundPrice;

    @Schema(description = "平台收取佣金")
    private BigDecimal commissionPrice;

    @Schema(description = "退单产生退还佣金金额")
    private BigDecimal refundCommissionPrice;

    @Schema(description = "分销返现支出")
    private BigDecimal distributionCommission;

    @Schema(description = "分销订单退还，返现佣金返还")
    private BigDecimal distributionRefundCommission;

    @Schema(description = "平台优惠券补贴")
    private BigDecimal siteCouponCommission;

    @Schema(description = "退货平台优惠券补贴返还")
    private BigDecimal siteCouponRefundCommission;

    @Schema(description = "平台优惠券 使用金额")
    private BigDecimal siteCouponPrice;

    @Schema(description = "平台优惠券 返点")
    private BigDecimal siteCouponPoint;
}
