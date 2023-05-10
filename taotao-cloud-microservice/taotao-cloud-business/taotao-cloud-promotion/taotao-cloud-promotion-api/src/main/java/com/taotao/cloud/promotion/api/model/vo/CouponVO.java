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

import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.CouponRangeDayEnum;
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 优惠券视图对象 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "优惠券")
public class CouponVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8372420376262437018L;

    private String couponName;

    /**
     * POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    private String couponType;

    /** 面额 */
    private BigDecimal price;

    /** 折扣 */
    private BigDecimal couponDiscount;

    /**
     * @see CouponGetEnum 优惠券类型，分为免费领取和活动赠送
     */
    private String getType;

    /** 店铺承担比例,平台发布时可以提供一定返点 */
    private BigDecimal storeCommission;

    /** 活动描述 */
    private String description;

    /** 发行数量,如果是0则是不限制 */
    private Integer publishNum;

    /** 领取限制 */
    private Integer couponLimitNum;

    /** 已被使用的数量 */
    private Integer usedNum;

    /** 已被领取的数量 */
    private Integer receivedNum;

    /** 消费门槛 */
    private BigDecimal consumeThreshold;

    /**
     * @see CouponRangeDayEnum 时间范围类型
     */
    private String rangeDayType;

    /** 有效期 */
    private Integer effectiveDays;

    // ********************************************************************************************

    /** 促销关联的商品 */
    @Schema(description = "优惠券关联商品集合")
    private List<PromotionGoodsVO> promotionGoodsList;

}
