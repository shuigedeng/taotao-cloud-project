package com.taotao.cloud.member.biz.roketmq.event.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.roketmq.event.MemberRegisterEvent;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册赠券活动
 */
@Component
public class RegisteredCouponActivityExecute implements MemberRegisterEvent {

	@Autowired
	private CouponActivityService couponActivityService;

	/**
	 * 获取进行中的注册赠券的优惠券活动 发送注册赠券
	 *
	 * @param member 会员
	 */
	@Override
	public void memberRegister(Member member) {
		List<CouponActivity> couponActivities = couponActivityService.list(
			new QueryWrapper<CouponActivity>()
				.eq("coupon_activity_type", CouponActivityTypeEnum.REGISTERED.name())
				.and(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START)));
		couponActivityService.registered(couponActivities, member);
	}
}
