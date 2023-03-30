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

package com.taotao.cloud.promotion.api.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 积分商品查询通用类 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsPageQuery extends BasePromotionsSearchQuery {

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品skuId")
    private String skuId;

    @Schema(description = "积分商品分类编号")
    private String pointsGoodsCategoryId;

    @Schema(description = "积分,可以为范围，如10_1000")
    private String points;

    // @Override
    // public <T> QueryWrapper<T> queryWrapper() {
    // 	QueryWrapper<T> queryWrapper = super.queryWrapper();
    // 	if (CharSequenceUtil.isNotEmpty(goodsName)) {
    // 		queryWrapper.like("goods_name", goodsName);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(skuId)) {
    // 		queryWrapper.eq("sku_id", skuId);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(pointsGoodsCategoryId)) {
    // 		queryWrapper.eq("points_goods_category_id", pointsGoodsCategoryId);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(points)) {
    // 		String[] s = points.split("_");
    // 		if (s.length > 1) {
    // 			queryWrapper.between("points", s[0], s[1]);
    // 		} else {
    // 			queryWrapper.eq("points", s[0]);
    // 		}
    // 	}
    // 	return queryWrapper;
    // }

}
