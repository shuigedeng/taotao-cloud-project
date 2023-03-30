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

package com.taotao.cloud.store.api.model.vo;

import com.taotao.cloud.store.api.enums.BillStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 结算单 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillVO {

    private String sn;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * @see BillStatusEnum
     */
    private String billStatus;

    private String storeId;

    private String storeName;

    private LocalDateTime payTime;

    private String bankAccountName;

    private String bankAccountNumber;

    private String bankName;

    private String bankCode;

    /**
     * 算钱规则 billPrice=orderPrice-refundPrice -commissionPrice+refundCommissionPrice
     * -distributionCommission+distributionRefundCommission
     * +siteCouponCommission-siteCouponRefundCommission +kanjiaSettlementPrice+pointSettlementPrice
     */
    private BigDecimal orderPrice;

    private BigDecimal refundPrice;

    private BigDecimal commissionPrice;

    private BigDecimal refundCommissionPrice;

    private BigDecimal distributionCommission;

    private BigDecimal distributionRefundCommission;

    private BigDecimal siteCouponCommission;

    private BigDecimal siteCouponRefundCommission;

    private BigDecimal pointSettlementPrice;

    private BigDecimal kanjiaSettlementPrice;

    private BigDecimal billPrice;
}
