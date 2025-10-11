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

package com.taotao.cloud.distribution.api.model.query;

import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.*;

/** 分销查询参数 */
@Getter
@Setter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销查询参数")
public class DistributionPageQuery extends PageQuery {

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "分销员状态", allowableValues = "APPLY,RETREAT,REFUSE,PASS")
    private String distributionStatus;

    // public <T> QueryWrapper<T> queryWrapper() {
    // 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    // 	queryWrapper.like(StringUtils.isNotEmpty(memberName), "member_name", memberName);
    // 	queryWrapper.eq(StringUtils.isNotEmpty(distributionStatus), "distribution_status",
    // 		distributionStatus);
    // 	return queryWrapper;
    // }
}
