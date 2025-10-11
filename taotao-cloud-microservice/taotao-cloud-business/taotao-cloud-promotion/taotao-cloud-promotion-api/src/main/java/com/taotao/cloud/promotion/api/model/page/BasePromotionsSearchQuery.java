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

package com.taotao.cloud.promotion.api.model.page;

import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotionsSearchQuery extends PageQuery implements Serializable {

    @Schema(description = "活动id")
    private String id;

    @Schema(description = "活动开始时间")
    private Long startTime;

    @Schema(description = "活动结束时间")
    private Long endTime;

    /**
     * @see PromotionsStatusEnum
     */
    @Schema(description = "活动状态")
    private String promotionStatus;

    /**
     * @see PromotionsScopeTypeEnum
     */
    @Schema(description = "关联范围类型")
    private String scopeType;

	public BasePromotionsSearchQuery baseSearchQuery(){
		return this;
	}
}
