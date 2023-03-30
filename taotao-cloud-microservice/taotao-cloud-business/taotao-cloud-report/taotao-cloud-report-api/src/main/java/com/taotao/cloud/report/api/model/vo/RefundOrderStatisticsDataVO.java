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

/** 退款统计VO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundOrderStatisticsDataVO {

    @Schema(description = "售后SN")
    private String refundSn;

    @Schema(description = "商家名称 ")
    private String storeName;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "规格内容")
    private String specs;

    @Schema(description = "实际退款金额")
    private BigDecimal finalPrice;
}
