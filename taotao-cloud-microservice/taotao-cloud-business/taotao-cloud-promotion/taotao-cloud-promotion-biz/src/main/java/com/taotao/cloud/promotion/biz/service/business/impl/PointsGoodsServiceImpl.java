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
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.goods.api.feign.IFeignEsGoodsIndexApi;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import com.taotao.cloud.promotion.biz.mapper.PointsGoodsMapper;
import com.taotao.cloud.promotion.biz.model.entity.PointsGoods;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.business.IPointsGoodsService;
import com.taotao.cloud.promotion.biz.service.business.IPromotionGoodsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 积分商品业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:46:32
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PointsGoodsServiceImpl extends AbstractPromotionsServiceImpl<PointsGoodsMapper, PointsGoods>
        implements IPointsGoodsService {

    /** 促销商品 */
    @Autowired
    private IPromotionGoodsService promotionGoodsService;
    /** 规格商品 */
    @Autowired
    private IFeignGoodsSkuApi goodsSkuApi;

    @Autowired
    private IFeignEsGoodsIndexApi esGoodsIndexApi;

    @Override
    public boolean savePointsGoodsBatch(List<PointsGoods> promotionsList) {
        List<PromotionGoods> promotionGoodsList = new ArrayList<>();
        Map<String, Long> skuPoints = new HashMap<>();
        for (PointsGoods pointsGoods : promotionsList) {
            this.initPromotion(pointsGoods);
            this.checkPromotions(pointsGoods);
            if (this.checkSkuDuplicate(pointsGoods.getSkuId(), null) == null) {
                pointsGoods.setPromotionName("积分商品活动");
            } else {
                throw new BusinessException("商品id为" + pointsGoods.getSkuId() + "的商品已参加积分商品活动！");
            }
            GoodsSku goodsSku = this.checkSkuExist(pointsGoods.getSkuId());
            pointsGoods.setStoreId(goodsSku.getStoreId());
            pointsGoods.setStoreName(goodsSku.getStoreName());
            PromotionGoods promotionGoods = new PromotionGoods(pointsGoods, goodsSku);
            promotionGoods.setPromotionType(PromotionTypeEnum.POINTS_GOODS.name());
            promotionGoodsList.add(promotionGoods);
            skuPoints.put(pointsGoods.getSkuId(), pointsGoods.getPoints());
        }
        boolean saveBatch = this.saveBatch(promotionsList);
        if (saveBatch) {
            this.promotionGoodsService.saveOrUpdateBatch(promotionGoodsList);
            for (Map.Entry<String, Long> entry : skuPoints.entrySet()) {
                Map<String, Object> query = MapUtil.builder(new HashMap<String, Object>())
                        .put("id", entry.getKey())
                        .build();
                Map<String, Object> update = MapUtil.builder(new HashMap<String, Object>())
                        .put("points", entry.getValue())
                        .build();
                this.esGoodsIndexApi.updateIndex(query, update);
            }
        }
        return saveBatch;
    }

    /**
     * 积分商品更新
     *
     * @param promotions 促销信息
     * @return 是否更新成功
     */
    @Override
    public boolean updatePromotions(PointsGoods promotions) {
        boolean result = false;
        this.checkStatus(promotions);
        this.checkPromotions(promotions);
        if (this.checkSkuDuplicate(promotions.getSkuId(), promotions.getId()) == null) {
            result = this.updateById(promotions);
            this.updatePromotionsGoods(promotions);
            this.updateEsGoodsIndex(promotions);
        }
        return result;
    }

    /**
     * 移除促销活动
     *
     * @param ids 促销活动id集合
     * @return 是否移除成功
     */
    @Override
    public boolean removePromotions(List<String> ids) {
        for (String id : ids) {
            PointsGoods pointsGoods = this.getById(id);
            if (pointsGoods == null) {
                log.error(ResultEnum.POINT_GOODS_NOT_EXIST.message());
                ids.remove(id);
            }
        }
        this.promotionGoodsService.deletePromotionGoods(ids);
        return this.removeByIds(ids);
    }

    /**
     * 根据ID获取积分详情
     *
     * @param id 积分商品id
     * @return 积分详情
     */
    @Override
    public PointsGoodsVO getPointsGoodsDetail(String id) {
        PointsGoods pointsGoods = this.checkExist(id);
        PointsGoodsVO pointsGoodsVO = new PointsGoodsVO();
        BeanUtils.copyProperties(pointsGoods, pointsGoodsVO);
        pointsGoodsVO.setGoodsSku(this.checkSkuExist(pointsGoods.getSkuId()));
        return pointsGoodsVO;
    }

    /**
     * 根据ID获取积分详情
     *
     * @param skuId 商品SkuId
     * @return 积分详情
     */
    @Override
    public PointsGoodsVO getPointsGoodsDetailBySkuId(String skuId) {
        PointsGoods pointsGoods =
                this.getOne(new LambdaQueryWrapper<PointsGoods>().eq(PointsGoods::getSkuId, skuId), false);
        if (pointsGoods == null) {
            log.error("skuId为" + skuId + "的积分商品不存在！");
            throw new BusinessException();
        }
        PointsGoodsVO pointsGoodsVO = new PointsGoodsVO();
        BeanUtils.copyProperties(pointsGoods, pointsGoodsVO);
        pointsGoodsVO.setGoodsSku(this.checkSkuExist(pointsGoods.getSkuId()));
        return pointsGoodsVO;
    }

    /**
     * 检查促销参数
     *
     * @param promotions 促销实体
     */
    @Override
    public void checkPromotions(PointsGoods promotions) {
        super.checkPromotions(promotions);
        GoodsSku goodsSku = this.checkSkuExist(promotions.getSkuId());
        if (promotions.getActiveStock() > goodsSku.getQuantity()) {
            throw new BusinessException(ResultEnum.POINT_GOODS_ACTIVE_STOCK_ERROR);
        }
    }

    /**
     * 检查促销状态
     *
     * @param promotions 促销实体
     */
    @Override
    public void checkStatus(PointsGoods promotions) {
        super.checkStatus(promotions);
    }

    /**
     * 更新促销商品信息
     *
     * @param promotions 促销实体
     */
    @Override
    public void updatePromotionsGoods(PointsGoods promotions) {
        this.promotionGoodsService.remove(
                new LambdaQueryWrapper<PromotionGoods>().eq(PromotionGoods::getPromotionId, promotions.getId()));
        this.promotionGoodsService.save(new PromotionGoods(promotions, this.checkSkuExist(promotions.getSkuId())));
    }

    /**
     * 更新促销信息到商品索引
     *
     * @param promotions 促销实体
     */
    @Override
    public void updateEsGoodsIndex(PointsGoods promotions) {
        Map<String, Object> query = MapUtil.builder(new HashMap<String, Object>())
                .put("id", promotions.getSkuId())
                .build();
        Map<String, Object> update = MapUtil.builder(new HashMap<String, Object>())
                .put("points", promotions.getPoints())
                .build();
        this.esGoodsIndexApi.updateIndex(query, update);
    }

    @Override
    public PromotionTypeEnum getPromotionType() {
        return PromotionTypeEnum.POINTS_GOODS;
    }

    /**
     * 检查积分商品存在
     *
     * @param id 积分商品id
     * @return 积分商品信息
     */
    private PointsGoods checkExist(String id) {
        PointsGoods pointsGoods = this.getById(id);
        if (pointsGoods == null) {
            log.error("id为" + id + "的积分商品不存在！");
            throw new BusinessException();
        }
        return pointsGoods;
    }

    /**
     * 检查积分商品是否重复存在
     *
     * @param skuId 商品SkuId
     * @param id 积分商品I（可选）
     * @return 积分商品信息
     */
    private PointsGoods checkSkuDuplicate(String skuId, String id) {
        QueryWrapper<PointsGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id", skuId);
        if (CharSequenceUtil.isNotEmpty(id)) {
            queryWrapper.ne("id", id);
        }
        queryWrapper.and(i -> i.or(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START))
                .or(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.NEW)));
        return this.getOne(queryWrapper, false);
    }

    /**
     * 检查商品Sku是否存
     *
     * @param skuId skuId
     * @return 商品sku
     */
    private GoodsSku checkSkuExist(String skuId) {
        GoodsSku goodsSku = this.goodsSkuApi.getGoodsSkuByIdFromCache(skuId);
        if (goodsSku == null) {
            log.error("商品ID为" + skuId + "的商品不存在！");
            throw new BusinessException();
        }
        return goodsSku;
    }
}
