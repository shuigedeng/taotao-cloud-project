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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.*;

/** 砍价活动搜索参数 */
@Getter
@Setter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivitySearchPageQuery extends BasePromotionsSearchQuery {


    @Schema(description = "砍价商品SkuID")
    private String kanjiaActivityGoodsId;

    @Schema(description = "会员ID", hidden = true)
    private String memberId;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "邀请活动ID，有值说明是被邀请人")
    private String kanjiaActivityId;

    @Schema(description = "规格商品ID", hidden = true)
    private String goodsSkuId;

    //
    // public <T> QueryWrapper<T> wrapper() {
    // 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    //
    // 	queryWrapper.eq(StrUtil.isNotEmpty(kanjiaActivityId), "id", kanjiaActivityId);
    // 	queryWrapper.eq(StrUtil.isNotEmpty(kanjiaActivityGoodsId), "kanjia_activity_goods_id",
    // kanjiaActivityGoodsId);
    // 	queryWrapper.eq(StrUtil.isNotEmpty(goodsSkuId), "sku_id", goodsSkuId);
    // 	queryWrapper.eq(StrUtil.isNotEmpty(memberId), "member_id", memberId);
    // 	queryWrapper.eq(StrUtil.isNotEmpty(status), "status", status);
    // 	return queryWrapper;
    // }
}
