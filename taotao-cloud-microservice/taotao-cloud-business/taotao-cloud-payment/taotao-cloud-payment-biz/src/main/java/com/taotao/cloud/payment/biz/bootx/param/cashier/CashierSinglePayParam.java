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

package com.taotao.cloud.payment.biz.bootx.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 结算台发起支付参数
 *
 * @author xxm
 * @date 2022/2/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "结算台单支付参数")
public class CashierSinglePayParam {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "支付通道")
    private Integer payChannel;

    @Schema(description = "支付方式")
    private Integer payWay;

    @Schema(description = "业务id")
    private String businessId;

    @Schema(description = "付款码")
    private String authCode;

    @Schema(description = "储值卡")
    private String voucherNo;
}
