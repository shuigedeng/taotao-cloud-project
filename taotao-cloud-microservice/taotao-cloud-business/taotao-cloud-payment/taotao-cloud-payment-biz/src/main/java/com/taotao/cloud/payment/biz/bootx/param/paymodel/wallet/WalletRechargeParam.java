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

package com.taotao.cloud.payment.biz.bootx.param.paymodel.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2020/12/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "钱包充值参数")
public class WalletRechargeParam implements Serializable {

    private static final long serialVersionUID = 73058709379178254L;

    @Schema(description = "钱包ID")
    private Long walletId;

    @Schema(description = "支付记录ID")
    private Long paymentId;

    @Schema(description = "充值金额")
    private BigDecimal amount;

    @Schema(description = "类型 2 主动充值 3 自动充值 4 admin充值")
    private Integer type;

    @Schema(description = "业务ID，对应的充值订单ID等")
    private String businessId;

    @Schema(description = "操作源")
    private Integer operationSource;

    @Schema(description = "订单id")
    private Long orderId;
}
