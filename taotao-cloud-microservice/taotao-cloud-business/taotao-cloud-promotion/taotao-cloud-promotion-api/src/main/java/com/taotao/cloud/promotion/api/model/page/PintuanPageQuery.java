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

import com.taotao.cloud.promotion.api.model.page.BasePromotionsSearchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 拼团查询通用类 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PintuanPageQuery extends BasePromotionsSearchQuery {

    @Schema(description = "商家id")
    private String storeId;

    @Schema(description = "商家名称，如果是平台，这个值为 platform")
    private String storeName;

    @NotEmpty(message = "活动名称不能为空")
    @Schema(description = "活动名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String promotionName;

    // @Override
    // public <T> QueryWrapper<T> queryWrapper() {
    // 	QueryWrapper<T> queryWrapper = super.queryWrapper();
    // 	if (CharSequenceUtil.isNotEmpty(promotionName)) {
    // 		queryWrapper.like("promotion_name", promotionName);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(storeName)) {
    // 		queryWrapper.like("store_name", storeName);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(storeId)) {
    // 		queryWrapper.eq("store_id", storeId);
    // 	}
    // 	return queryWrapper;
    // }

}
