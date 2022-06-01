package com.taotao.cloud.promotion.biz.service;


import com.taotao.cloud.member.api.vo.MemberVO;
import com.taotao.cloud.promotion.api.vo.CouponActivityVO;
import com.taotao.cloud.promotion.biz.entity.CouponActivity;

import java.util.List;

/**
 * 优惠券活动业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:22
 */
public interface CouponActivityService extends AbstractPromotionsService<CouponActivity> {


	/**
	 * 获取优惠券活动VO
	 * 包含优惠券活动信息以及优惠券关联优惠券列表
	 *
	 * @param couponActivityId 优惠券活动ID
	 * @return {@link CouponActivityVO }
	 * @since 2022-04-27 16:43:22
	 */
	CouponActivityVO getCouponActivityVO(String couponActivityId);

	/**
	 * 精准发券
	 *
	 * @param couponActivityId 优惠券活动ID
	 * @since 2022-04-27 16:43:22
	 */
	void specify(Long couponActivityId);

	/**
	 * 注册赠券
	 *
	 * @param couponActivityList 优惠券活动
	 * @param member             会员
	 * @since 2022-04-27 16:43:22
	 */
	void registered(List<CouponActivity> couponActivityList, MemberVO member);

}
