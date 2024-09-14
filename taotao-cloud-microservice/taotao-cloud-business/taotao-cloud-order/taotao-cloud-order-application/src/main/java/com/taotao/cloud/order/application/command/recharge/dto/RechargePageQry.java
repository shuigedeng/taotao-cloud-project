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

package com.taotao.cloud.order.application.command.recharge.dto;

import com.taotao.boot.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 预存款充值记录查询条件 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "预存款充值记录查询条件")
public class RechargePageQry extends PageQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 318396158590640917L;

    @Schema(description = "充值订单编号")
    private String rechargeSn;

    @Schema(description = "会员Id")
    private String memberId;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "充值开始时间")
    private String startDate;

    @Schema(description = "充值结束时间")
    private String endDate;
}
