package com.taotao.cloud.promotion.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.promotion.api.model.vo.CouponActivityItemVO;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivityItem;

import java.util.List;

/**
 * 优惠券活动-优惠券业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:14
 */
public interface ICouponActivityItemService extends IService<CouponActivityItem> {

	/**
	 * 获取优惠券活动关联优惠券列表
	 *
	 * @param activityId 优惠券活动ID
	 * @return {@link List }<{@link CouponActivityItem }>
	 * @since 2022-04-27 16:43:14
	 */
	List<CouponActivityItem> getCouponActivityList(Long activityId);

	/**
	 * 获取优惠券活动关联优惠券列表VO
	 *
	 * @param activityId 优惠券活动ID
	 * @return {@link List }<{@link CouponActivityItemVO }>
	 * @since 2022-04-27 16:43:14
	 */
	List<CouponActivityItemVO> getCouponActivityItemListVO(String activityId);

	/**
	 * 根据优惠券id删除优惠活动关联信息项
	 *
	 * @param couponIds 优惠券id集合
	 * @since 2022-04-27 16:43:14
	 */
	void removeByCouponId(List<String> couponIds);
}
