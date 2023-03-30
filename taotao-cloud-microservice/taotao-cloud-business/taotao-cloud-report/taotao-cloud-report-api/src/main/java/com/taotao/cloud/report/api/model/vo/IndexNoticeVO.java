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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 消息提示 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexNoticeVO {

    @Schema(description = "待处理商品审核")
    private Long goods;

    @Schema(description = "待处理店铺入驻审核")
    private Long store;

    @Schema(description = "待处理售后申请")
    private Long refund;

    @Schema(description = "待处理投诉审核")
    private Long complain;

    @Schema(description = "待处理分销员提现申请")
    private Long distributionCash;

    @Schema(description = "待处理商家结算")
    private Long waitPayBill;
}
