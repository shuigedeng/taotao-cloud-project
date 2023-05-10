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
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 满优惠查询通用类 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullDiscountPageQuery extends BasePromotionsSearchQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -4052716630253333681L;

    @Schema(description = "活动名称")
    private String promotionName;

    @Schema(description = "店铺编号 如有多个','分割")
    private String storeId;

    @Schema(description = "是否赠优惠券")
    private Boolean isCoupon;

    @Schema(description = "优惠券id")
    private String couponId;

    // @Override
    // public <T> QueryWrapper<T> queryWrapper() {
    //     QueryWrapper<T> queryWrapper = super.queryWrapper();
    //     if (CharSequenceUtil.isNotEmpty(promotionName)) {
    //         queryWrapper.like("title", promotionName);
    //     }
    //     if (storeId != null) {
    //         queryWrapper.in("store_id", Arrays.asList(storeId.split(",")));
    //     }
    //     if (isCoupon != null) {
    //         queryWrapper.eq("is_coupon", isCoupon);
    //     }
    //     if (CharSequenceUtil.isNotEmpty(couponId)) {
    //         queryWrapper.eq("coupon_id", couponId);
    //     }
    //     return queryWrapper;
    // }

}
