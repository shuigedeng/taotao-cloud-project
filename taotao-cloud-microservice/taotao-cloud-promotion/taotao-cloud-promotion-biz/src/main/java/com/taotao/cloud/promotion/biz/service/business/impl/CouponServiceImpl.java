package com.taotao.cloud.promotion.biz.service.business.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import com.taotao.cloud.promotion.api.enums.CouponRangeDayEnum;
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import com.taotao.cloud.promotion.api.model.query.CouponPageQuery;
import com.taotao.cloud.promotion.api.model.query.FullDiscountPageQuery;
import com.taotao.cloud.promotion.api.model.query.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.vo.CouponVO;
import com.taotao.cloud.promotion.biz.mapper.CouponMapper;
import com.taotao.cloud.promotion.biz.model.entity.Coupon;
import com.taotao.cloud.promotion.biz.model.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.business.CouponActivityItemService;
import com.taotao.cloud.promotion.biz.service.business.CouponService;
import com.taotao.cloud.promotion.biz.service.business.FullDiscountService;
import com.taotao.cloud.promotion.biz.service.business.MemberCouponService;
import com.taotao.cloud.promotion.biz.service.business.PromotionGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券活动业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponServiceImpl extends AbstractPromotionsServiceImpl<CouponMapper, Coupon> implements
	CouponService {

	/**
	 * 规格商品
	 */
	@Autowired
	private IFeignGoodsSkuApi goodsSkuService;
	/**
	 * 促销商品
	 */
	@Autowired
	private PromotionGoodsService promotionGoodsService;
	/**
	 * 会员优惠券
	 */
	@Autowired
	private MemberCouponService memberCouponService;
	/**
	 * 满额活动
	 */
	@Autowired
	private FullDiscountService fullDiscountService;
	/**
	 * 优惠券活动-优惠券关联
	 */
	@Autowired
	private CouponActivityItemService couponActivityItemService;

	@Override
	public void receiveCoupon(Long couponId, Integer receiveNum) {
		Coupon coupon = this.getById(couponId);
		if (coupon == null) {
			throw new BusinessException(ResultEnum.COUPON_NOT_EXIST);
		}
		this.update(new LambdaUpdateWrapper<Coupon>().eq(Coupon::getId, coupon.getId()).set(Coupon::getReceivedNum,
			coupon.getReceivedNum() + receiveNum));
	}

	@Override
	public boolean removePromotions(List<String> ids) {
		//删除优惠券信息
		this.memberCouponService.closeMemberCoupon(ids);

		//删除优惠券活动关联优惠券
		this.couponActivityItemService.removeByCouponId(ids);
		return super.removePromotions(ids);
	}

	@Override
	public void usedCoupon(String couponId, Integer usedNum) {
		Coupon coupon = this.getById(couponId);
		if (coupon == null) {
			throw new BusinessException(ResultEnum.COUPON_NOT_EXIST);
		}

		this.update(new LambdaUpdateWrapper<Coupon>().eq(Coupon::getId, coupon.getId()).set(Coupon::getUsedNum,
			coupon.getUsedNum() + usedNum));
	}

	@Override
	public IPage<CouponVO> pageVOFindAll(CouponPageQuery searchParams, PageParam page) {
		IPage<Coupon> couponIPage = super.pageFindAll(searchParams, page);
		List<CouponVO> couponVOList = couponIPage.getRecords().stream().map(CouponVO::new).collect(Collectors.toList());
		return PageUtil.convertPage(couponIPage, couponVOList);
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
	public boolean updateStatus(List<String> ids, Long startTime, Long endTime) {
		List<Coupon> list = this.list(new LambdaQueryWrapper<Coupon>().in(Coupon::getId, ids).eq(Coupon::getRangeDayType, CouponRangeDayEnum.DYNAMICTIME.name()));
		if (!list.isEmpty()) {
			LambdaUpdateWrapper<Coupon> updateWrapper = new LambdaUpdateWrapper<>();
			updateWrapper.in(Coupon::getId, list.stream().map(Coupon::getId).collect(Collectors.toList()));
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
		//优惠券限制领取数量
		if (promotions.getCouponLimitNum() < 0) {
			throw new BusinessException(ResultEnum.COUPON_LIMIT_NUM_LESS_THAN_0);
		}
		//如果发行数量是0则判断领取限制数量
		if (promotions.getPublishNum() != 0 && promotions.getCouponLimitNum() > promotions.getPublishNum()) {
			throw new BusinessException(ResultEnum.COUPON_LIMIT_GREATER_THAN_PUBLISH);
		}
		//打折优惠券大于10折
		boolean discountCoupon = (promotions.getCouponType().equals(CouponTypeEnum.DISCOUNT.name())
			&& (promotions.getCouponDiscount() < 0 && promotions.getCouponDiscount() > 10));
		if (discountCoupon) {
			throw new BusinessException(ResultEnum.COUPON_DISCOUNT_ERROR);
		}

		//优惠券为固定时间类型
		if (promotions.getRangeDayType() != null && promotions.getRangeDayType().equals(CouponRangeDayEnum.FIXEDTIME.name())) {
			long nowTime = DateUtils.getDateline() * 1000;
			//固定时间的优惠券不能小于当前时间
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
		if (!PromotionsStatusEnum.CLOSE.name().equals(promotions.getPromotionStatus()) &&
			PromotionsScopeTypeEnum.PORTION_GOODS.name().equals(promotions.getScopeType()) &&
			promotions instanceof CouponVO) {
			CouponVO couponVO = (CouponVO) promotions;
			this.promotionGoodsService.deletePromotionGoods(Collections.singletonList(promotions.getId()));
			List<PromotionGoods> promotionGoodsList = PromotionTools.promotionGoodsInit(couponVO.getPromotionGoodsList(), couponVO, this.getPromotionType());
			//促销活动商品更新
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
			&& (coupon.getPromotionGoodsList() == null || coupon.getPromotionGoodsList().isEmpty()));
		if (portionGoodsScope) {
			throw new BusinessException(ResultEnum.COUPON_SCOPE_TYPE_GOODS_ERROR);
		} else if (coupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS.name()) && CharSequenceUtil.isEmpty(coupon.getScopeId())) {
			throw new BusinessException(ResultEnum.COUPON_SCOPE_TYPE_GOODS_ERROR);
		} else if (coupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS_CATEGORY.name()) && CharSequenceUtil.isEmpty(coupon.getScopeId())) {
			throw new BusinessException(ResultEnum.COUPON_SCOPE_TYPE_CATEGORY_ERROR);
		} else if (coupon.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_SHOP_CATEGORY.name()) && CharSequenceUtil.isEmpty(coupon.getScopeId())) {
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
			GoodsSkuSpecGalleryVO goodsSku = goodsSkuService.getGoodsSkuByIdFromCache(Long.valueOf(id)).data();
			if (goodsSku == null) {
				throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
			}
		}
	}

}
