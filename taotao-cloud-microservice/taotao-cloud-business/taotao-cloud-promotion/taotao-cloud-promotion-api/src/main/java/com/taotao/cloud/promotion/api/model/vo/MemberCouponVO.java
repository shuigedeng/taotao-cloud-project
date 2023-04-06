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
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** MemberCouponVO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCouponVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7814832369110695758L;

    /** 从哪个模版领取的优惠券 */
    private Long couponId;
    /** 商家id，如果是平台发送，这个值为 0 */
    private Long storeId;
    /** 商家名称，如果是平台，这个值为 platfor */
    private String storeName;
    /** 面额 */
    private BigDecimal price;
    /** 折扣 */
    private BigDecimal discount;
    /** 消费门槛 */
    private BigDecimal consumeThreshold;
    /** 会员名称 */
    private String memberName;
    /** 会员id */
    private Long memberId;

    /**
     * 关联范围类型
     *
     * @see PromotionsScopeTypeEnum
     */
    private String scopeType;

    /**
     * 活动类型 POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    private String couponType;

    /** 范围关联的id */
    private Long scopeId;
    /** 使用起始时间 */
    private LocalDateTime startTime;
    /** 使用截止时间 */
    private LocalDateTime endTime;
    /**
     * 优惠券类型，分为免费领取和活动赠送
     *
     * @see CouponGetEnum
     */
    private String getType;
    /** 是否是平台优惠券 */
    private Boolean isPlatform;
    /** 店铺承担比例 */
    private BigDecimal storeCommission;
    /** 核销时间 */
    private LocalDateTime consumptionTime;

    /**
     * 会员优惠券状态
     *
     * @see MemberCouponStatusEnum
     */
    private String memberCouponStatus;

    // ***********************************************************************************************************

    @Schema(description = "无法使用原因")
    private String reason;
}
