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

package com.taotao.cloud.promotion.api.model.vo;

import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 优惠券活动的优惠券VO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivityItemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    /** 优惠券活动ID */
    private Long activityId;
    /** 优惠券ID */
    private Long couponId;
    /** 优惠券数量 */
    private Integer num;

    // *************************************************************************************

    @Schema(description = "优惠券名称")
    private String couponName;

    @Schema(description = "面额")
    private BigDecimal price;

    /**
     * POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    @Schema(description = "优惠券类型")
    private String couponType;

    @Schema(description = "折扣")
    private BigDecimal couponDiscount;
}
