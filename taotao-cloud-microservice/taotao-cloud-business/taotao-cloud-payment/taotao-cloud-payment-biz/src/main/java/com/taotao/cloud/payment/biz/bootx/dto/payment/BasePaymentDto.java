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

package com.taotao.cloud.payment.biz.bootx.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2021/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "具体支付日志基类")
public class BasePaymentDto extends BaseDto {

    @Schema(description = "支付id")
    private Long paymentId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "关联的业务id")
    private String businessId;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "可退款金额")
    private BigDecimal refundableBalance;

    /**
     * @see PayStatusCode
     */
    @Schema(description = "支付状态")
    private int payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;
}
