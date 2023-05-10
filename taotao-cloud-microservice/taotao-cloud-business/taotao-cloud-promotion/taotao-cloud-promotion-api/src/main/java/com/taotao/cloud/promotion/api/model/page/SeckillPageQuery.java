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

import com.taotao.cloud.promotion.api.enums.PromotionsApplyStatusEnum;
import com.taotao.cloud.promotion.api.model.page.BasePromotionsSearchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 秒杀活动查询通用类 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillPageQuery extends BasePromotionsSearchQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -4052716630253333681L;

    @Schema(description = "秒杀活动活动编号")
    private String seckillId;

    @Schema(description = "活动名称")
    private String promotionName;

    @Schema(description = "时刻")
    private Integer timeLine;

    @Schema(description = "商家id")
    private String[] storeIds;

    @Schema(description = "商家编号")
    private String storeId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商家编号")
    private String skuId;

    /**
     * @see PromotionsApplyStatusEnum
     */
    @Schema(description = "APPLY(\"申请\"), PASS(\"通过\"), REFUSE(\"拒绝\")")
    private String promotionApplyStatus;

    // @Override
    // public <T> QueryWrapper<T> queryWrapper() {
    // 	QueryWrapper<T> queryWrapper = super.queryWrapper();
    // 	if (CharSequenceUtil.isNotEmpty(goodsName)) {
    // 		queryWrapper.like("goods_name", goodsName);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(promotionName)) {
    // 		queryWrapper.like("promotion_name", promotionName);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(seckillId)) {
    // 		queryWrapper.eq("seckill_id", seckillId);
    // 	}
    // 	if (storeIds != null) {
    // 		queryWrapper.in("store_id", Arrays.asList(storeIds));
    // 	}
    // 	if (timeLine != null) {
    // 		queryWrapper.eq("time_line", timeLine);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(promotionApplyStatus)) {
    // 		queryWrapper.eq("promotion_apply_status",
    // PromotionsApplyStatusEnum.valueOf(promotionApplyStatus).name());
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(skuId)) {
    // 		queryWrapper.eq("sku_id", skuId);
    // 	}
    // 	return queryWrapper;
    // }

}
