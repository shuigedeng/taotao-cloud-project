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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 促销商品查询通用类 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionGoodsPageQuery extends BasePromotionsSearchQuery {

    @Schema(description = "促销活动id")
    private Long promotionId;

    @Schema(description = "促销类型")
    private String promotionType;

    @Schema(description = "商品活动id")
    private String storeId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品分类路径")
    private String categoryPath;

    @Schema(description = "商品SkuId")
    private String skuId;

    @Schema(description = "商品SkuIds")
    private List<Long> skuIds;

    @Schema(description = "促销活动id")
    private List<Long> promotionIds;

    // @Override
    // public <T> QueryWrapper<T> queryWrapper() {
    // 	if (CharSequenceUtil.isEmpty(this.getScopeType())) {
    // 		this.setScopeType(PromotionsScopeTypeEnum.PORTION_GOODS.name());
    // 	}
    // 	QueryWrapper<T> queryWrapper = super.queryWrapper();
    // 	if (Objects.nonNull(promotionId)) {
    // 		queryWrapper.eq("promotion_id", promotionId);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(goodsName)) {
    // 		queryWrapper.like("goods_name", goodsName);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(promotionType)) {
    // 		queryWrapper.eq("promotion_type", promotionType);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(categoryPath)) {
    // 		queryWrapper.like("category_path", categoryPath);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(storeId)) {
    // 		queryWrapper.in("store_id", Arrays.asList(storeId.split(",")));
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(skuId)) {
    // 		queryWrapper.in("sku_id", Arrays.asList(skuId.split(",")));
    // 	}
    // 	if (skuIds != null && !skuIds.isEmpty()) {
    // 		queryWrapper.in("sku_id", skuIds);
    // 	}
    // 	if (promotionIds != null && promotionIds.isEmpty()) {
    // 		queryWrapper.in("promotion_id", promotionIds);
    // 	}
    // 	return queryWrapper;
    // }

}
