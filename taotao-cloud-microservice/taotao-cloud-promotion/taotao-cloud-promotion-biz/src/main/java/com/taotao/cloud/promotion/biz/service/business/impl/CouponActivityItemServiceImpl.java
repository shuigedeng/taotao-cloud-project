package com.taotao.cloud.promotion.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.promotion.api.model.vo.CouponActivityItemVO;
import com.taotao.cloud.promotion.biz.model.entity.CouponActivityItem;
import com.taotao.cloud.promotion.biz.mapper.CouponActivityItemMapper;
import com.taotao.cloud.promotion.biz.service.business.CouponActivityItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券活动关联优惠券业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:01
 */
@Service
public class CouponActivityItemServiceImpl extends ServiceImpl<CouponActivityItemMapper, CouponActivityItem> implements
	CouponActivityItemService {

	@Override
	public List<CouponActivityItem> getCouponActivityList(Long activityId) {
		LambdaQueryWrapper<CouponActivityItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(CouponActivityItem::getActivityId, activityId);
		return this.list(lambdaQueryWrapper);
	}

	@Override
	public List<CouponActivityItemVO> getCouponActivityItemListVO(String activityId) {
		return this.baseMapper.getCouponActivityItemListVO(activityId);
	}

	/**
	 * 根据优惠券id删除优惠活动关联信息项
	 *
	 * @param couponIds 优惠券id集合
	 */
	@Override
	public void removeByCouponId(List<String> couponIds) {
		this.remove(new LambdaQueryWrapper<CouponActivityItem>()
			.in(CouponActivityItem::getCouponId, couponIds));
	}
}
