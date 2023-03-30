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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.base.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 基础支付记录类
 *
 * @author xxm
 * @date 2021/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BasePayment extends MpBaseEntity {

    /** 交易记录ID */
    private Long paymentId;

    /** 用户ID */
    private Long userId;

    /** 交易金额 */
    private BigDecimal amount;

    /** 可退款金额 */
    private BigDecimal refundableBalance;

    /** 关联的业务id */
    private String businessId;

    /**
     * 支付状态
     *
     * @see PayStatusCode
     */
    private Integer payStatus;

    /** 支付时间 */
    private LocalDateTime payTime;
}
