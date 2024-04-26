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

package com.taotao.cloud.auth.common.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 分销佣金查询信息 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销佣金查询信息")
public class DistributionCashSearchVO {

    @Schema(description = "编号")
    private String sn;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "分销员提现状态", allowableValues = "APPLY,PASS,REFUSE")
    private String distributionCashStatus;

    // public <T> QueryWrapper<T> queryWrapper() {
    // 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    // 	if (StringUtils.isNotEmpty(memberName)) {
    // 		queryWrapper.like("distribution_name", memberName);
    // 	}
    // 	if (StringUtils.isNotEmpty(sn)) {
    // 		queryWrapper.like("sn", sn);
    // 	}
    // 	if (StringUtils.isNotEmpty(distributionCashStatus)) {
    // 		queryWrapper.like("distribution_cash_status", distributionCashStatus);
    // 	}
    // 	return queryWrapper;
    // }

}
