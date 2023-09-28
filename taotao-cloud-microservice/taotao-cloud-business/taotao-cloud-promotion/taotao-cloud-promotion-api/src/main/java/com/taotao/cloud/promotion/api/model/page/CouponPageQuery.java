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

import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.model.page.BasePromotionsSearchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/** 优惠券查询通用类 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true, fluent = false)
public class CouponPageQuery extends BasePromotionsSearchQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 4566880169478260409L;

    private static final String PRICE_COLUMN = "price";
    private static final String RANGE_DAY_TYPE_COLUMN = "range_day_type";

    @Schema(description = "店铺编号")
    private String storeId;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "优惠券名称")
    private String couponName;
    /**
     * POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    @Schema(description = "活动类型")
    private String couponType;
    /**
     * @see PromotionsScopeTypeEnum
     */
//    @Schema(description = "关联范围类型")
//    private String scopeType;

    @Schema(description = "范围关联的id")
    private String scopeId;

    @Schema(description = "面额,可以为范围，如10_1000")
    private String price;

    @Schema(description = "发行数量,可以为范围，如10_1000")
    private String publishNum;

    @Schema(description = "已被领取的数量,可以为范围，如10_1000")
    private String receivedNum;
    /**
     * @see CouponGetEnum
     */
    @Schema(description = "优惠券类型，分为免费领取和活动赠送")
    private String getType;
    /**
     * @see MemberCouponStatusEnum
     */
    @Schema(description = "会员优惠券状态")
    private String memberCouponStatus;

    // @Override
    // public <T> QueryWrapper<T> queryWrapper() {
    // 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    // 	if (storeId != null) {
    // 		queryWrapper.in("store_id", Collections.singletonList(storeId));
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(couponName)) {
    // 		queryWrapper.like("coupon_name", couponName);
    // 	}
    // 	if (memberId != null) {
    // 		queryWrapper.eq("member_id", memberId);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(couponType)) {
    // 		queryWrapper.eq("coupon_type", CouponTypeEnum.valueOf(couponType).name());
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(scopeType)) {
    // 		queryWrapper.eq("scope_type", PromotionsScopeTypeEnum.valueOf(scopeType).name());
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(scopeId)) {
    // 		queryWrapper.eq("scope_id", scopeId);
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(getType)) {
    // 		queryWrapper.eq("get_type", CouponGetEnum.valueOf(getType).name());
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(memberCouponStatus)) {
    // 		queryWrapper.eq("member_coupon_status",
    // 			MemberCouponStatusEnum.valueOf(memberCouponStatus).name());
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(this.getPromotionStatus())) {
    // 		switch (PromotionsStatusEnum.valueOf(this.getPromotionStatus())) {
    // 			case NEW -> queryWrapper.nested(i -> i.gt(PromotionTools.START_TIME_COLUMN, new Date())
    // 				.gt(PromotionTools.END_TIME_COLUMN, new Date()))
    // 			;
    // 			case START -> queryWrapper.nested(i -> i.le(PromotionTools.START_TIME_COLUMN, new Date())
    // 					.ge(PromotionTools.END_TIME_COLUMN, new Date()))
    // 				.or(i -> i.gt("effective_days", 0)
    // 					.eq(RANGE_DAY_TYPE_COLUMN, CouponRangeDayEnum.DYNAMICTIME.name()));
    // 			case END -> queryWrapper.nested(i -> i.lt(PromotionTools.START_TIME_COLUMN, new Date())
    // 				.lt(PromotionTools.END_TIME_COLUMN, new Date()));
    // 			case CLOSE -> queryWrapper.nested(n -> n.nested(
    // 					i -> i.isNull(PromotionTools.START_TIME_COLUMN)
    // 						.isNull(PromotionTools.END_TIME_COLUMN)
    // 						.eq(RANGE_DAY_TYPE_COLUMN, CouponRangeDayEnum.FIXEDTIME.name())).
    // 				or(i -> i.le("effective_days", 0)
    // 					.eq(RANGE_DAY_TYPE_COLUMN, CouponRangeDayEnum.DYNAMICTIME.name())));
    // 			default -> {
    // 			}
    // 		}
    // 	}
    // 	if (this.getStartTime() != null) {
    // 		queryWrapper.ge("start_time", new Date(this.getEndTime()));
    // 	}
    // 	if (this.getEndTime() != null) {
    // 		queryWrapper.le("end_time", new Date(this.getEndTime()));
    // 	}
    // 	queryWrapper.eq("delete_flag", false);
    // 	this.betweenWrapper(queryWrapper);
    // 	queryWrapper.orderByDesc("create_time");
    // 	return queryWrapper;
    // }
    //
    // private <T> void betweenWrapper(QueryWrapper<T> queryWrapper) {
    // 	if (CharSequenceUtil.isNotEmpty(publishNum)) {
    // 		String[] s = publishNum.split("_");
    // 		if (s.length > 1) {
    // 			queryWrapper.ge("publish_num", s[1]);
    // 		} else {
    // 			queryWrapper.le("publish_num", publishNum);
    // 		}
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(price)) {
    // 		String[] s = price.split("_");
    // 		if (s.length > 1) {
    // 			queryWrapper.ge(PRICE_COLUMN, s[1]);
    // 		} else {
    // 			queryWrapper.le(PRICE_COLUMN, s[0]);
    // 		}
    // 	}
    // 	if (CharSequenceUtil.isNotEmpty(receivedNum)) {
    // 		String[] s = receivedNum.split("_");
    // 		if (s.length > 1) {
    // 			queryWrapper.ge("received_num", s[1]);
    // 		} else {
    // 			queryWrapper.le("received_num", s[0]);
    // 		}
    // 	}
    // }

}
