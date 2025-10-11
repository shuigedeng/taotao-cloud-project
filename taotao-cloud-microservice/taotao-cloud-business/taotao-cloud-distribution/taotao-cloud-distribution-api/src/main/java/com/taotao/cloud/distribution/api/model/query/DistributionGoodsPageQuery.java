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

/** 分销员商品查询条件 */
@Setter
@Getter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分销员商品查询条件")
public class DistributionGoodsPageQuery extends PageQuery {

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "是否已选择")
    private boolean isChecked;

    // public <T> QueryWrapper<T> queryWrapper() {
    // 	QueryWrapper<T> queryWrapper = this.distributionQueryWrapper();
    // 	queryWrapper.eq(CharSequenceUtil.isNotEmpty(goodsId), "goods_id", goodsId);
    // 	queryWrapper.eq(CharSequenceUtil.isNotEmpty(goodsName), "goods_name", goodsId);
    // 	return queryWrapper;
    // }
    //
    // public <T> QueryWrapper<T> storeQueryWrapper() {
    // 	QueryWrapper<T> queryWrapper = this.distributionQueryWrapper();
    // 	queryWrapper.eq("dg.store_id", SecurityUtils.getCurrentUser().getStoreId());
    // 	return queryWrapper;
    // }
    //
    // public <T> QueryWrapper<T> distributionQueryWrapper() {
    // 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    // 	queryWrapper.like(CharSequenceUtil.isNotEmpty(goodsName), "dg.goods_name", goodsName);
    // 	return queryWrapper;
    // }

}
