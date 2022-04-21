package com.taotao.cloud.promotion.api.query;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.experimental.SuperBuilder;

/**
 * 会员优惠券查询通用类
 */
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCouponSearchParams extends BasePromotionsSearchParams implements Serializable {

	@Serial
	private static final long serialVersionUID = 4566880169478260409L;

	private static final String PRICE_COLUMN = "price";

	@Schema(description = "会员id")
	private String memberId;
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
	@Schema(description = "关联范围类型")
	private String scopeType;
	@Schema(description = "范围关联的id")
	private String scopeId;
	@Schema(description = "面额,可以为范围，如10_1000")
	private String price;
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
	@Schema(description = "消费门槛")
	private BigDecimal consumeThreshold;


	@Override
	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = super.queryWrapper();
		if (CharSequenceUtil.isNotEmpty(couponType)) {
			queryWrapper.eq("coupon_type", CouponTypeEnum.valueOf(couponType).name());
		}
		if (memberId != null) {
			queryWrapper.eq("member_id", memberId);
		}
		if (CharSequenceUtil.isNotEmpty(scopeId)) {
			queryWrapper.eq("scope_id", scopeId);
		}
		if (CharSequenceUtil.isNotEmpty(scopeType)) {
			queryWrapper.eq("scope_type", PromotionsScopeTypeEnum.valueOf(scopeType).name());
		}
		//if (CharSequenceUtil.isNotEmpty(getType)) {
		//    queryWrapper.eq("get_type", CouponGetEnum.valueOf(getType).name());
		//}
		if (CharSequenceUtil.isNotEmpty(memberCouponStatus)) {
			queryWrapper.eq("member_coupon_status", MemberCouponStatusEnum.valueOf(memberCouponStatus).name());
		}
		if (CharSequenceUtil.isNotEmpty(price)) {
			String[] s = price.split("_");
			if (s.length > 1) {
				queryWrapper.between(PRICE_COLUMN, s[0], s[1]);
			} else {
				queryWrapper.ge(PRICE_COLUMN, s[0]);
			}
		}
		if (this.getStartTime() != null) {
			queryWrapper.ge("start_time", new Date(this.getEndTime()));
		}
		if (this.getEndTime() != null) {
			queryWrapper.le("end_time", new Date(this.getEndTime()));
		}
		queryWrapper.eq("delete_flag", false);
		queryWrapper.orderByDesc("create_time");
		return queryWrapper;
	}


}
