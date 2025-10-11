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

package com.taotao.cloud.promotion.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.utils.date.DateUtils;
import com.taotao.boot.data.mybatis.mybatisplus.utils.PageUtils;
import com.taotao.cloud.goods.api.feign.GoodsSkuApi;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import com.taotao.cloud.promotion.api.enums.CouponRangeDayEnum;
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.CouponPageQuery;
import com.taotao.cloud.promotion.api.model.page.FullDiscountPageQuery;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.vo.CouponVO;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import com.taotao.cloud.promotion.biz.mapper.CouponMapper;
import com.taotao.cloud.promotion.biz.model.entity.Coupon;
import com.taotao.cloud.promotion.biz.model.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 优惠券活动业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponServiceImpl extends AbstractPromotionsServiceImpl<CouponMapper, Coupon> implements ICouponService {

	/**
	 * 规格商品
	 */
	@Autowired
	private GoodsSkuApi goodsSkuApi;
	/**
	 * 促销商品
	 */
	@Autowired
	private IPromotionGoodsService promotionGoodsService;
	/**
	 * 会员优惠券
	 */
	@Autowired
	private IMemberCouponService memberCouponService;
	/**
	 * 满额活动
	 */
	@Autowired
	private IFullDiscountService fullDiscountService;
	/**
	 * 优惠券活动-优惠券关联
	 */
	@Autowired
	private ICouponActivityItemService couponActivityItemService;

	@Override
	public void receiveCoupon(Long couponId, Integer receiveNum) {
		Coupon coupon = this.getById(couponId);
		if (coupon == null) {
			throw new BusinessException(ResultEnum.COUPON_NOT_EXIST);
		}
		this.update(new LambdaUpdateWrapper<Coupon>()
			.eq(Coupon::getId, coupon.getId())
			.set(Coupon::getReceivedNum, coupon.getReceivedNum() + receiveNum));
	}

	@Override
	public boolean removePromotions(List<String> ids) {
		// 删除优惠券信息
		this.memberCouponService.closeMemberCoupon(ids);

		// 删除优惠券活动关联优惠券
		this.couponActivityItemService.removeByCouponId(ids);
		return super.removePromotions(ids);
	}

	@Override
	public void usedCoupon(String couponId, Integer usedNum) {
		Coupon coupon = this.getById(couponId);
		if (coupon == null) {
			throw new BusinessException(ResultEnum.COUPON_NOT_EXIST);
		}

		this.update(new LambdaUpdateWrapper<Coupon>()
			.eq(Coupon::getId, coupon.getId())
			.set(Coupon::getUsedNum, coupon.getUsedNum() + usedNum));
	}

	@Override
	public IPage<CouponVO> pageVOFindAll(CouponPageQuery searchParams, PageQuery page) {
		IPage<Coupon> couponIPage = super.pageFindAll(searchParams, page);
		return PageUtils.convertPage(couponIPage, e -> BeanUtil.copyProperties(e, CouponVO.class));
	}

	@Override
	public CouponVO getDetail(String couponId) {
		CouponVO couponVO = new CouponVO(this.getById(couponId));
		PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
		searchParams.setPromotionId(couponId);
		List<PromotionGoods> promotionsByPromotionId = this.promotionGoodsService.listFindAll(searchParams);
		if (promotionsByPromotionId != null && !promotionsByPromotionId.isEmpty()) {
			couponVO.setPromotionGoodsList(promotionsByPromotionId);
		}
		return couponVO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateStatus(List<String> ids, Long startTime, Long endTime) {
		List<Coupon> list = this.list(new LambdaQueryWrapper<Coupon>()
			.in(Coupon::getId, ids)
			.eq(Coupon::getRangeDayType, CouponRangeDayEnum.DYNAMICTIME.name()));
		if (!list.isEmpty()) {
			LambdaUpdateWrapper<Coupon> updateWrapper = new LambdaUpdateWrapper<>();
			List<Long> couponIds = list
				.stream()
				.map(Coupon::getId)
				.toList();
			updateWrapper.in(Coupon::getId, couponIds);
			updateWrapper.set(Coupon::getEffectiveDays, 0);
			this.update(updateWrapper);
		}

		return super.updateStatus(ids, startTime, endTime);
	}

	@Override
	public void initPromotion(Coupon promotions) {
		promotions.setUsedNum(0);
		promotions.setReceivedNum(0);
	}

	@Override
	public void checkPromotions(Coupon promotions) {
		if (promotions.getRangeDayType() == null) {
			super.checkPromotions(promotions);
		}
		// 优惠券限制领取数量
		if (promotions.getCouponLimitNum() < 0) {
			throw new BusinessException(ResultEnum.COUPON_LIMIT_NUM_LESS_THAN_0);
		}
		// 如果发行数量是0则判断领取限制数量
		if (promotions.getPublishNum() != 0 && promotions.getCouponLimitNum() > promotions.getPublishNum()) {
			throw new BusinessException(ResultEnum.COUPON_LIMIT_GREATER_THAN_PUBLISH);
		}
		// 打折优惠券大于10折
		boolean discountCoupon = (promotions.getCouponType().equals(CouponTypeEnum.DISCOUNT.name())
			&& (promotions.getCouponDiscount() < 0 && promotions.getCouponDiscount() > 10));
		if (discountCoupon) {
			throw new BusinessException(ResultEnum.COUPON_DISCOUNT_ERROR);
		}

		// 优惠券为固定时间类型
		if (promotions.getRangeDayType() != null
			&& promotions.getRangeDayType().equals(CouponRangeDayEnum.FIXEDTIME.name())) {
			long nowTime = DateUtils.getDateline() * 1000;
			// 固定时间的优惠券不能小于当前时间
			if (promotions.getEndTime().getTime() < nowTime) {
				throw new BusinessException(ResultEnum.PROMOTION_END_TIME_ERROR);
			}
		}

		this.checkCouponScope((CouponVO) promotions);
	}

	@Override
	public void checkStatus(Coupon promotions) {
		super.checkStatus(promotions);
		FullDiscountPageQuery searchParams = new FullDiscountPageQuery();
		searchParams.setIsCoupon(true);
		searchParams.setCouponId(promotions.getId());
		List<FullDiscount> fullDiscounts = fullDiscountService.listFindAll(searchParams);
		if (fullDiscounts != null && !fullDiscounts.isEmpty()) {
			throw new BusinessException("当前优惠券参与了促销活动【" + fullDiscounts.get(0).getPromotionName() + "】不能进行编辑删除操作");
		}
	}

	@Override
	public void updatePromotionsGoods(Coupon promotions) {
		super.updatePromotionsGoods(promotions);
		if (!PromotionsStatusEnum.CLOSE.name().equals(promotions.getPromotionStatus())
			&& PromotionsScopeTypeEnum.PORTION_GOODS.name().equals(promotions.getScopeType())
			&& promotions instanceof CouponVO) {
			CouponVO couponVO = (CouponVO) promotions;
			this.promotionGoodsService.deletePromotionGoods(Collections.singletonList(promotions.getId()));
			List<PromotionGoods> promotionGoodsList = PromotionTools.promotionGoodsInit(
				couponVO.getPromotionGoodsList(), couponVO, this.getPromotionType());
			// 促销活动商品更新
			this.promotionGoodsService.saveBatch(promotionGoodsList);
		}
	}

	@Override
	public void updateEsGoodsIndex(Coupon promotions) {
		Coupon coupon = JSONUtil.parse(promotions).toBean(Coupon.class);
		super.updateEsGoodsIndex(coupon);
	}

	@Override
	public PromotionTypeEnum getPromotionType() {
		return PromotionTypeEnum.COUPON;
	}

	/**
	 * 检查优惠券范围
	 *
	 * @param coupon 检查的优惠券对象
	 */
	private void checkCouponScope(CouponVO coupon) {
		boolean portionGoodsScope = (coupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS.name())
			&& (coupon.getPromotionGoodsList() == null
			|| coupon.getPromotionGoodsList().isEmpty()));
		if (portionGoodsScope) {
			throw new BusinessException(ResultEnum.COUPON_SCOPE_TYPE_GOODS_ERROR);
		} else if (coupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS.name())
			&& CharSequenceUtil.isEmpty(coupon.getScopeId())) {
			throw new BusinessException(ResultEnum.COUPON_SCOPE_TYPE_GOODS_ERROR);
		} else if (coupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS_CATEGORY.name())
			&& CharSequenceUtil.isEmpty(coupon.getScopeId())) {
			throw new BusinessException(ResultEnum.COUPON_SCOPE_TYPE_CATEGORY_ERROR);
		} else if (coupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_SHOP_CATEGORY.name())
			&& CharSequenceUtil.isEmpty(coupon.getScopeId())) {
			throw new BusinessException(ResultEnum.COUPON_SCOPE_TYPE_STORE_ERROR);
		}

		if (coupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS.name())) {
			this.checkCouponPortionGoods(coupon);
		}
	}

	/**
	 * 检查指定商品
	 *
	 * @param coupon 优惠券信息
	 */
	private void checkCouponPortionGoods(CouponVO coupon) {
		String[] split = coupon.getScopeId().split(",");
		if (split.length <= 0) {
			throw new BusinessException(ResultEnum.COUPON_SCOPE_ERROR);
		}
		for (String id : split) {
			GoodsSkuSpecGalleryVO goodsSku = goodsSkuApi.getGoodsSkuByIdFromCache(Long.valueOf(id));
			if (goodsSku == null) {
				throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
			}
		}
	}
}
