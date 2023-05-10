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

import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.CouponPageQuery;
import com.taotao.cloud.promotion.api.model.page.FullDiscountPageQuery;
import com.taotao.cloud.promotion.api.model.page.PintuanPageQuery;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.page.SeckillPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.Coupon;
import com.taotao.cloud.promotion.biz.model.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.model.entity.Pintuan;
import com.taotao.cloud.promotion.biz.model.entity.PointsGoods;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.model.entity.Seckill;
import com.taotao.cloud.promotion.biz.model.entity.SeckillApply;
import com.taotao.cloud.promotion.biz.service.business.ICouponService;
import com.taotao.cloud.promotion.biz.service.business.IFullDiscountService;
import com.taotao.cloud.promotion.biz.service.business.IPintuanService;
import com.taotao.cloud.promotion.biz.service.business.IPointsGoodsService;
import com.taotao.cloud.promotion.biz.service.business.IPromotionGoodsService;
import com.taotao.cloud.promotion.biz.service.business.IPromotionService;
import com.taotao.cloud.promotion.biz.service.business.ISeckillApplyService;
import com.taotao.cloud.promotion.biz.service.business.ISeckillService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 促销业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:39
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PromotionServiceImpl implements IPromotionService {

    /** 秒杀 */
    @Autowired
    private ISeckillService seckillService;
    /** 秒杀申请 */
    @Autowired
    private ISeckillApplyService seckillApplyService;
    /** 满额活动 */
    @Autowired
    private IFullDiscountService fullDiscountService;
    /** 拼团 */
    @Autowired
    private IPintuanService pintuanService;
    /** 优惠券 */
    @Autowired
    private ICouponService couponService;
    /** 促销商品 */
    @Autowired
    private IPromotionGoodsService promotionGoodsService;
    /** 积分商品 */
    @Autowired
    private IPointsGoodsService pointsGoodsService;

    @Autowired
    private IFeignGoodsSkuApi goodsSkuApi;

    /**
     * 获取当前进行的所有促销活动信息
     *
     * @return 当前促销活动集合
     */
    @Override
    public Map<String, Object> getCurrentPromotion() {
        Map<String, Object> resultMap = new HashMap<>(16);

        SeckillPageQuery seckillSearchParams = new SeckillPageQuery();
        seckillSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        // 获取当前进行的秒杀活动活动
        List<Seckill> seckillList = seckillService.listFindAll(seckillSearchParams);
        if (seckillList != null && !seckillList.isEmpty()) {
            for (Seckill seckill : seckillList) {
                resultMap.put(PromotionTypeEnum.SECKILL.name(), seckill);
            }
        }
        FullDiscountPageQuery fullDiscountSearchParams = new FullDiscountPageQuery();
        fullDiscountSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        // 获取当前进行的满优惠活动
        List<FullDiscount> fullDiscountList = fullDiscountService.listFindAll(fullDiscountSearchParams);
        if (fullDiscountList != null && !fullDiscountList.isEmpty()) {
            for (FullDiscount fullDiscount : fullDiscountList) {
                resultMap.put(PromotionTypeEnum.FULL_DISCOUNT.name(), fullDiscount);
            }
        }
        PintuanPageQuery pintuanSearchParams = new PintuanPageQuery();
        pintuanSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        // 获取当前进行的拼团活动
        List<Pintuan> pintuanList = pintuanService.listFindAll(pintuanSearchParams);
        if (pintuanList != null && !pintuanList.isEmpty()) {
            for (Pintuan pintuan : pintuanList) {
                resultMap.put(PromotionTypeEnum.PINTUAN.name(), pintuan);
            }
        }
        return resultMap;
    }

    /**
     * 根据商品索引获取当前商品索引的所有促销活动信息
     *
     * @param index 商品索引
     * @return 当前促销活动集合
     */
    @Override
    public Map<String, Object> getGoodsCurrentPromotionMap(EsGoodsIndex index) {
        Map<String, Object> promotionMap = new HashMap<>();
        FullDiscountPageQuery fullDiscountSearchParams = new FullDiscountPageQuery();
        fullDiscountSearchParams.setScopeType(PromotionsScopeTypeEnum.ALL.name());
        fullDiscountSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        List<FullDiscount> fullDiscountVOS = this.fullDiscountService.listFindAll(fullDiscountSearchParams);
        for (FullDiscount fullDiscount : fullDiscountVOS) {
            if (index.getStoreId().equals(fullDiscount.getStoreId())) {
                String fullDiscountKey = PromotionTypeEnum.FULL_DISCOUNT.name() + "-" + fullDiscount.getId();
                promotionMap.put(fullDiscountKey, fullDiscount);
            }
        }
        CouponPageQuery couponSearchParams = new CouponPageQuery();
        couponSearchParams.setScopeType(PromotionsScopeTypeEnum.ALL.name());
        couponSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        List<Coupon> couponVOS = this.couponService.listFindAll(couponSearchParams);
        for (Coupon coupon : couponVOS) {
            if (("platform").equals(coupon.getStoreId()) || index.getStoreId().equals(coupon.getStoreId())) {
                String couponKey = PromotionTypeEnum.COUPON.name() + "-" + coupon.getId();
                promotionMap.put(couponKey, coupon);
            }
        }
        PromotionGoodsPageQuery promotionGoodsSearchParams = new PromotionGoodsPageQuery();
        promotionGoodsSearchParams.setSkuId(index.getId());
        promotionGoodsSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        List<PromotionGoods> promotionGoodsList = promotionGoodsService.listFindAll(promotionGoodsSearchParams);
        for (PromotionGoods promotionGoods : promotionGoodsList) {
            String esPromotionKey = promotionGoods.getPromotionType() + "-" + promotionGoods.getPromotionId();
            switch (PromotionTypeEnum.valueOf(promotionGoods.getPromotionType())) {
                case COUPON:
                    Coupon coupon = couponService.getById(promotionGoods.getPromotionId());
                    promotionMap.put(esPromotionKey, coupon);
                    break;
                case PINTUAN:
                    Pintuan pintuan = pintuanService.getById(promotionGoods.getPromotionId());
                    promotionMap.put(esPromotionKey, pintuan);
                    index.setPromotionPrice(promotionGoods.getPrice());
                    break;
                case FULL_DISCOUNT:
                    FullDiscount fullDiscount = fullDiscountService.getById(promotionGoods.getPromotionId());
                    promotionMap.put(esPromotionKey, fullDiscount);
                    break;
                case SECKILL:
                    this.getGoodsCurrentSeckill(promotionGoods, promotionMap, index);
                    break;
                case POINTS_GOODS:
                    PointsGoods pointsGoods = pointsGoodsService.getById(promotionGoods.getPromotionId());
                    promotionMap.put(esPromotionKey, pointsGoods);
                    break;
                default:
                    break;
            }
        }
        return promotionMap;
    }

    private void getGoodsCurrentSeckill(
            PromotionGoods promotionGoods, Map<String, Object> promotionMap, EsGoodsIndex index) {
        Seckill seckill = seckillService.getById(promotionGoods.getPromotionId());
        SeckillPageQuery searchParams = new SeckillPageQuery();
        searchParams.setSeckillId(promotionGoods.getPromotionId());
        searchParams.setSkuId(promotionGoods.getSkuId());
        List<SeckillApply> seckillApplyList = seckillApplyService.getSeckillApply(searchParams);
        if (seckillApplyList != null && !seckillApplyList.isEmpty()) {
            SeckillApply seckillApply = seckillApplyList.get(0);
            int nextHour = 23;
            String[] split = seckill.getHours().split(",");
            int[] hoursSored = Arrays
				.stream(split)
				.mapToInt(Integer::parseInt).toArray();
            Arrays.sort(hoursSored);
            for (int i : hoursSored) {
                if (seckillApply.getTimeLine() < i) {
                    nextHour = i;
                }
            }
            String seckillKey = promotionGoods.getPromotionType() + "-" + nextHour;
            seckill.setStartTime(promotionGoods.getStartTime());
            seckill.setEndTime(promotionGoods.getEndTime());
            promotionMap.put(seckillKey, seckill);
            index.setPromotionPrice(promotionGoods.getPrice());
        }
    }
}
