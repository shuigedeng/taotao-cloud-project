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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.order.api.model.vo.cart.CartSkuVO;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.BasePromotionsSearchQuery;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import com.taotao.cloud.promotion.biz.mapper.PromotionGoodsMapper;
import com.taotao.cloud.promotion.biz.model.entity.Coupon;
import com.taotao.cloud.promotion.biz.model.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.model.entity.SeckillApply;
import com.taotao.cloud.promotion.biz.service.business.ICouponService;
import com.taotao.cloud.promotion.biz.service.business.IFullDiscountService;
import com.taotao.cloud.promotion.biz.service.business.IPromotionGoodsService;
import com.taotao.cloud.promotion.biz.service.business.ISeckillApplyService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 促销商品业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PromotionGoodsServiceImpl extends ServiceImpl<PromotionGoodsMapper, PromotionGoods>
        implements IPromotionGoodsService {

    /** Redis */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /** 秒杀活动申请 */
    @Autowired
    private ISeckillApplyService seckillApplyService;
    /** 规格商品 */
    @Autowired
    private IFeignGoodsSkuApi goodsSkuApi;

    @Autowired
    private IFullDiscountService fullDiscountService;

    @Autowired
    private ICouponService couponService;

    @Override
    public List<PromotionGoods> findNowSkuPromotion(String skuId) {

        GoodsSku sku = goodsSkuApi.getGoodsSkuByIdFromCache(skuId);
        if (sku == null) {
            return new ArrayList<>();
        }
        QueryWrapper<PromotionGoods> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("sku_id", skuId);
        queryWrapper.and(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START));

        List<PromotionGoods> promotionGoods = this.list(queryWrapper);

        BasePromotionsSearchQuery searchParams = new BasePromotionsSearchQuery();
        searchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        searchParams.setScopeType(PromotionsScopeTypeEnum.ALL.name());
        // 单独检查，添加适用于全品类的满优惠活动
        List<FullDiscount> fullDiscountVOS = this.fullDiscountService.listFindAll(searchParams);
        for (FullDiscount fullDiscountVO : fullDiscountVOS) {
            PromotionGoods p = new PromotionGoods(sku);
            p.setPromotionId(fullDiscountVO.getId());
            p.setPromotionType(PromotionTypeEnum.FULL_DISCOUNT.name());
            p.setStartTime(fullDiscountVO.getStartTime());
            p.setEndTime(fullDiscountVO.getEndTime());
            promotionGoods.add(p);
        }
        // 单独检查，添加适用于全品类的全平台或属于当前店铺的优惠券活动
        List<Coupon> couponVOS = this.couponService.listFindAll(searchParams);
        for (Coupon couponVO : couponVOS) {
            PromotionGoods p = new PromotionGoods(sku);
            p.setPromotionId(couponVO.getId());
            p.setPromotionType(PromotionTypeEnum.COUPON.name());
            p.setStartTime(couponVO.getStartTime());
            p.setEndTime(couponVO.getEndTime());
            promotionGoods.add(p);
        }
        return promotionGoods;
    }

    @Override
    public void updatePromotion(CartSkuVO cartSkuVO) {
        Date date = DateUtils.getCurrentDayEndTime();
        // 如果商品的促销更新时间在当前时间之前，则更新促销
        if (cartSkuVO.getUpdatePromotionTime().before(date)) {
            List<PromotionGoods> promotionGoods =
                    this.findNowSkuPromotion(cartSkuVO.getGoodsSku().getId());
            cartSkuVO.setPromotions(promotionGoods);
            // 下一次更新时间
            cartSkuVO.setUpdatePromotionTime(date);
        }
    }

    @Override
    public IPage<PromotionGoods> pageFindAll(PromotionGoodsPageQuery searchParams, PageVO pageVo) {
        return this.page(PageUtil.initPage(pageVo), searchParams.queryWrapper());
    }

    /**
     * 获取促销商品信息
     *
     * @param searchParams 查询参数
     * @return 促销商品列表
     */
    @Override
    public List<PromotionGoods> listFindAll(PromotionGoodsPageQuery searchParams) {
        return this.list(searchParams.queryWrapper());
    }

    /**
     * 获取促销商品信息
     *
     * @param searchParams 查询参数
     * @return 促销商品信息
     */
    @Override
    public PromotionGoods getPromotionsGoods(PromotionGoodsPageQuery searchParams) {
        return this.getOne(searchParams.queryWrapper(), false);
    }

    /**
     * 获取当前有效时间特定促销类型的促销商品信息
     *
     * @param skuId 查询参数
     * @param promotionTypes 特定促销类型
     * @return 促销商品信息
     */
    @Override
    public PromotionGoods getValidPromotionsGoods(String skuId, List<String> promotionTypes) {
        QueryWrapper<PromotionGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id", skuId);
        queryWrapper.in("promotion_type", promotionTypes);
        queryWrapper.and(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START));
        return this.getOne(queryWrapper, false);
    }

    /**
     * 获取当前有效时间特定促销类型的促销商品价格
     *
     * @param skuId skuId
     * @param promotionTypes 特定促销类型
     * @return 促销商品价格
     */
    @Override
    public BigDecimal getValidPromotionsGoodsPrice(String skuId, List<String> promotionTypes) {
        QueryWrapper<PromotionGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id", skuId);
        queryWrapper.in("promotion_type", promotionTypes);
        queryWrapper.and(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START));
        return this.baseMapper.selectPromotionsGoodsPrice(queryWrapper);
    }

    @Override
    public Integer findInnerOverlapPromotionGoods(
            String promotionType, String skuId, Date startTime, Date endTime, String promotionId) {
        if (promotionId != null) {
            return this.baseMapper.selectInnerOverlapPromotionGoodsWithout(
                    promotionType, skuId, startTime, endTime, promotionId);
        } else {
            return this.baseMapper.selectInnerOverlapPromotionGoods(promotionType, skuId, startTime, endTime);
        }
    }

    /**
     * 获取促销活动商品库存
     *
     * @param typeEnum 促销商品类型
     * @param promotionId 促销活动id
     * @param skuId 商品skuId
     * @return 促销活动商品库存
     */
    @Override
    public Integer getPromotionGoodsStock(PromotionTypeEnum typeEnum, String promotionId, String skuId) {
        String promotionStockKey = IPromotionGoodsService.getPromotionGoodsStockCacheKey(typeEnum, promotionId, skuId);
        String promotionGoodsStock = stringRedisTemplate.opsForValue().get(promotionStockKey);

        // 库存如果不为空，则直接返回
        if (promotionGoodsStock != null && CharSequenceUtil.isNotEmpty(promotionGoodsStock)) {
            return Convert.toInt(promotionGoodsStock);
        }
        // 如果为空
        else {
            // 获取促销商品，如果不存在促销商品，则返回0
            PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
            searchParams.setPromotionType(typeEnum.name());
            searchParams.setPromotionId(promotionId);
            searchParams.setSkuId(skuId);
            PromotionGoods promotionGoods = this.getPromotionsGoods(searchParams);
            if (promotionGoods == null) {
                return 0;
            }
            // 否则写入新的促销商品库存
            stringRedisTemplate
                    .opsForValue()
                    .set(promotionStockKey, promotionGoods.getQuantity().toString());
            return promotionGoods.getQuantity();
        }
    }

    @Override
    public List<Integer> getPromotionGoodsStock(PromotionTypeEnum typeEnum, String promotionId, List<String> skuId) {
        PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
        searchParams.setPromotionType(typeEnum.name());
        searchParams.setPromotionId(promotionId);
        searchParams.setSkuIds(skuId);
        // 获取促销商品，如果不存在促销商品，则返回0
        List<PromotionGoods> promotionGoods = this.listFindAll(searchParams);
        // 接收数据
        List<Integer> result = new ArrayList<>(skuId.size());
        for (String sid : skuId) {
            Integer stock = null;
            for (PromotionGoods pg : promotionGoods) {
                if (sid.equals(pg.getSkuId())) {
                    stock = pg.getQuantity();
                }
            }
            // 如果促销商品不存在，给一个默认值
            if (stock == null) {
                stock = 0;
            }
            result.add(stock);
        }
        return result;
    }

    /**
     * 更新促销活动商品库存
     *
     * @param typeEnum 促销商品类型
     * @param promotionId 促销活动id
     * @param skuId 商品skuId
     * @param quantity 更新后的库存数量
     */
    @Override
    public void updatePromotionGoodsStock(
            PromotionTypeEnum typeEnum, String promotionId, String skuId, Integer quantity) {
        String promotionStockKey = IPromotionGoodsService.getPromotionGoodsStockCacheKey(typeEnum, promotionId, skuId);
        if (typeEnum.equals(PromotionTypeEnum.SECKILL)) {
            LambdaQueryWrapper<SeckillApply> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SeckillApply::getSeckillId, promotionId).eq(SeckillApply::getSkuId, skuId);
            SeckillApply seckillApply = this.seckillApplyService.getOne(queryWrapper, false);
            if (seckillApply == null) {
                throw new BusinessException(ResultEnum.SECKILL_NOT_EXIST_ERROR);
            }
            LambdaUpdateWrapper<SeckillApply> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SeckillApply::getSeckillId, promotionId).eq(SeckillApply::getSkuId, skuId);
            updateWrapper.set(SeckillApply::getQuantity, quantity);
            seckillApplyService.update(updateWrapper);
        } else {
            LambdaUpdateWrapper<PromotionGoods> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .eq(PromotionGoods::getPromotionType, typeEnum.name())
                    .eq(PromotionGoods::getPromotionId, promotionId)
                    .eq(PromotionGoods::getSkuId, skuId);
            updateWrapper.set(PromotionGoods::getQuantity, quantity);
            this.update(updateWrapper);
        }

        stringRedisTemplate.opsForValue().set(promotionStockKey, quantity.toString());
    }

    /**
     * 更新促销活动商品库存
     *
     * @param promotionGoods 促销信息
     */
    @Override
    public void updatePromotionGoodsByPromotions(PromotionGoods promotionGoods) {
        LambdaQueryWrapper<PromotionGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromotionGoods::getPromotionId, promotionGoods.getPromotionId());
        this.remove(queryWrapper);
        this.save(promotionGoods);
    }

    /**
     * 删除促销商品
     *
     * @param promotionId 促销活动id
     * @param skuIds skuId
     */
    @Override
    public void deletePromotionGoods(String promotionId, List<String> skuIds) {
        LambdaQueryWrapper<PromotionGoods> queryWrapper = new LambdaQueryWrapper<PromotionGoods>()
                .eq(PromotionGoods::getPromotionId, promotionId)
                .in(PromotionGoods::getSkuId, skuIds);
        this.remove(queryWrapper);
    }

    /**
     * 删除促销促销商品
     *
     * @param promotionIds 促销活动id
     */
    @Override
    public void deletePromotionGoods(List<Long> promotionIds) {
        LambdaQueryWrapper<PromotionGoods> queryWrapper =
                new LambdaQueryWrapper<PromotionGoods>().in(PromotionGoods::getPromotionId, promotionIds);
        this.remove(queryWrapper);
    }

    /**
     * 根据参数删除促销商品
     *
     * @param searchParams 查询参数
     */
    @Override
    public void deletePromotionGoods(PromotionGoodsPageQuery searchParams) {
        this.remove(searchParams.queryWrapper());
    }
}
