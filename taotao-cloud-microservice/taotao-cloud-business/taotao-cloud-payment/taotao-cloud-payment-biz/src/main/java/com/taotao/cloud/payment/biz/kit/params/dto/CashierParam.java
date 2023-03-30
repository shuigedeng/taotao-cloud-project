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

package com.taotao.cloud.payment.biz.kit.params.dto;

import com.taotao.cloud.common.utils.lang.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/** 支付参数 */
@Data
public class CashierParam {

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "支付title")
    private String title;

    @Schema(description = "支付详细描述")
    private String detail;

    @Schema(description = "订单sn集合")
    private String orderSns;

    @Schema(description = "支持支付方式")
    private List<String> support;

    @Schema(description = "订单创建时间")
    private LocalDateTime createTime;

    @Schema(description = "支付自动结束时间")
    private Long autoCancel;

    @Schema(description = "剩余余额")
    private BigDecimal walletValue;

    public String getDetail() {
        if (StringUtils.isEmpty(detail)) {
            return "清单详细";
        }
        return StringUtils.filterSpecialChart(detail);
    }
}
