package com.taotao.cloud.promotion.biz.service.impl;

import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuService;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.query.CouponSearchParams;
import com.taotao.cloud.promotion.api.query.FullDiscountSearchParams;
import com.taotao.cloud.promotion.api.query.PintuanSearchParams;
import com.taotao.cloud.promotion.api.query.PromotionGoodsSearchParams;
import com.taotao.cloud.promotion.api.query.SeckillSearchParams;
import com.taotao.cloud.promotion.biz.entity.Coupon;
import com.taotao.cloud.promotion.biz.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.entity.Pintuan;
import com.taotao.cloud.promotion.biz.entity.PointsGoods;
import com.taotao.cloud.promotion.biz.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.entity.Seckill;
import com.taotao.cloud.promotion.biz.entity.SeckillApply;
import com.taotao.cloud.promotion.biz.service.CouponService;
import com.taotao.cloud.promotion.biz.service.FullDiscountService;
import com.taotao.cloud.promotion.biz.service.PintuanService;
import com.taotao.cloud.promotion.biz.service.PointsGoodsService;
import com.taotao.cloud.promotion.biz.service.PromotionGoodsService;
import com.taotao.cloud.promotion.biz.service.PromotionService;
import com.taotao.cloud.promotion.biz.service.SeckillApplyService;
import com.taotao.cloud.promotion.biz.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销业务层实现
 *
 *
 * @since 2020/8/21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PromotionServiceImpl implements PromotionService {
    /**
     * 秒杀
     */
    @Autowired
    private SeckillService seckillService;
    /**
     * 秒杀申请
     */
    @Autowired
    private SeckillApplyService seckillApplyService;
    /**
     * 满额活动
     */
    @Autowired
    private FullDiscountService fullDiscountService;
    /**
     * 拼团
     */
    @Autowired
    private PintuanService pintuanService;
    /**
     * 优惠券
     */
    @Autowired
    private CouponService couponService;
    /**
     * 促销商品
     */
    @Autowired
    private PromotionGoodsService promotionGoodsService;
    /**
     * 积分商品
     */
    @Autowired
    private PointsGoodsService pointsGoodsService;

    @Autowired
    private IFeignGoodsSkuService goodsSkuService;


    /**
     * 获取当前进行的所有促销活动信息
     *
     * @return 当前促销活动集合
     */
    @Override
    public Map<String, Object> getCurrentPromotion() {
        Map<String, Object> resultMap = new HashMap<>(16);

        SeckillSearchParams seckillSearchParams = new SeckillSearchParams();
        seckillSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        //获取当前进行的秒杀活动活动
        List<Seckill> seckillList = seckillService.listFindAll(seckillSearchParams);
        if (seckillList != null && !seckillList.isEmpty()) {
            for (Seckill seckill : seckillList) {
                resultMap.put(PromotionTypeEnum.SECKILL.name(), seckill);
            }
        }
        FullDiscountSearchParams fullDiscountSearchParams = new FullDiscountSearchParams();
        fullDiscountSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        //获取当前进行的满优惠活动
        List<FullDiscount> fullDiscountList = fullDiscountService.listFindAll(fullDiscountSearchParams);
        if (fullDiscountList != null && !fullDiscountList.isEmpty()) {
            for (FullDiscount fullDiscount : fullDiscountList) {
                resultMap.put(PromotionTypeEnum.FULL_DISCOUNT.name(), fullDiscount);
            }
        }
        PintuanSearchParams pintuanSearchParams = new PintuanSearchParams();
        pintuanSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        //获取当前进行的拼团活动
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
        FullDiscountSearchParams fullDiscountSearchParams = new FullDiscountSearchParams();
        fullDiscountSearchParams.setScopeType(PromotionsScopeTypeEnum.ALL.name());
        fullDiscountSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        List<FullDiscount> fullDiscountVOS = this.fullDiscountService.listFindAll(fullDiscountSearchParams);
        for (FullDiscount fullDiscount : fullDiscountVOS) {
            if (index.getStoreId().equals(fullDiscount.getStoreId())) {
                String fullDiscountKey = PromotionTypeEnum.FULL_DISCOUNT.name() + "-" + fullDiscount.getId();
                promotionMap.put(fullDiscountKey, fullDiscount);
            }
        }
        CouponSearchParams couponSearchParams = new CouponSearchParams();
        couponSearchParams.setScopeType(PromotionsScopeTypeEnum.ALL.name());
        couponSearchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        List<Coupon> couponVOS = this.couponService.listFindAll(couponSearchParams);
        for (Coupon coupon : couponVOS) {
            if (("platform").equals(coupon.getStoreId()) || index.getStoreId().equals(coupon.getStoreId())) {
                String couponKey = PromotionTypeEnum.COUPON.name() + "-" + coupon.getId();
                promotionMap.put(couponKey, coupon);
            }
        }
        PromotionGoodsSearchParams promotionGoodsSearchParams = new PromotionGoodsSearchParams();
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


    private void getGoodsCurrentSeckill(PromotionGoods promotionGoods, Map<String, Object> promotionMap, EsGoodsIndex index) {
        Seckill seckill = seckillService.getById(promotionGoods.getPromotionId());
        SeckillSearchParams searchParams = new SeckillSearchParams();
        searchParams.setSeckillId(promotionGoods.getPromotionId());
        searchParams.setSkuId(promotionGoods.getSkuId());
        List<SeckillApply> seckillApplyList = seckillApplyService.getSeckillApply(searchParams);
        if (seckillApplyList != null && !seckillApplyList.isEmpty()) {
            SeckillApply seckillApply = seckillApplyList.get(0);
            int nextHour = 23;
            String[] split = seckill.getHours().split(",");
            int[] hoursSored = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
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
