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

package com.taotao.cloud.promotion.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.order.api.model.vo.cart.CartSkuVO;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 促销商品业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:13
 */
public interface IPromotionGoodsService extends IService<PromotionGoods> {

    /**
     * 缓存商品库存key
     *
     * @param typeEnum 促销分类枚举
     * @param promotionId 促销活动Id
     * @param skuId skuId
     * @return {@link String }
     * @since 2022-04-27 16:44:13
     */
    static String getPromotionGoodsStockCacheKey(PromotionTypeEnum typeEnum, String promotionId, String skuId) {
        return "{"
                + CachePrefix.PROMOTION_GOODS_STOCK.name()
                + "_"
                + typeEnum.name()
                + "}_"
                + promotionId
                + "_"
                + skuId;
    }

    /**
     * 更新促销活动
     *
     * @param cartSkuVO 购物车中的产品
     * @since 2022-04-27 16:44:13
     */
    void updatePromotion(CartSkuVO cartSkuVO);

    /**
     * 获取某sku当日所有活动
     *
     * @param skuId 商品skuId
     * @return {@link List }<{@link PromotionGoods }>
     * @since 2022-04-27 16:44:13
     */
    List<PromotionGoods> findNowSkuPromotion(String skuId);

    /**
     * 分页获取促销商品信息
     *
     * @param searchParams 查询参数
     * @param pageQuery 分页参数
     * @return {@link IPage }<{@link PromotionGoods }>
     * @since 2022-04-27 16:44:13
     */
    IPage<PromotionGoods> pageFindAll(PromotionGoodsPageQuery searchParams, PageQuery pageQuery);

    /**
     * 获取促销商品信息
     *
     * @param searchParams 查询参数
     * @return {@link List }<{@link PromotionGoods }>
     * @since 2022-04-27 16:44:13
     */
    List<PromotionGoods> listFindAll(PromotionGoodsPageQuery searchParams);

    /**
     * 获取促销商品信息
     *
     * @param searchParams 查询参数
     * @return {@link PromotionGoods }
     * @since 2022-04-27 16:44:13
     */
    PromotionGoods getPromotionsGoods(PromotionGoodsPageQuery searchParams);

    /**
     * 获取当前有效时间特定促销类型的促销商品信息
     *
     * @param skuId skuId
     * @param promotionTypes 特定促销类型
     * @return {@link PromotionGoods }
     * @since 2022-04-27 16:44:14
     */
    PromotionGoods getValidPromotionsGoods(String skuId, List<String> promotionTypes);

    /**
     * 获取当前有效时间特定促销类型的促销商品价格
     *
     * @param skuId skuId
     * @param promotionTypes 特定促销类型
     * @return {@link BigDecimal }
     * @since 2022-04-27 16:44:14
     */
    BigDecimal getValidPromotionsGoodsPrice(String skuId, List<String> promotionTypes);

    /**
     * 查询参加活动促销商品是否同时参加指定类型的活动
     *
     * @param promotionType 促销类型
     * @param skuId skuId
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param promotionId 促销活动id(是否排除当前活动，如排除，则填写，没有的话，为null)
     * @return {@link Integer }
     * @since 2022-04-27 16:44:14
     */
    Integer findInnerOverlapPromotionGoods(
            String promotionType, String skuId, Date startTime, Date endTime, String promotionId);

    /**
     * 获取促销活动商品库存
     *
     * @param typeEnum 促销商品类型
     * @param promotionId 促销活动id
     * @param skuId 商品skuId
     * @return {@link Integer }
     * @since 2022-04-27 16:44:14
     */
    Integer getPromotionGoodsStock(PromotionTypeEnum typeEnum, String promotionId, String skuId);

    /**
     * 批量获取促销活动商品库存
     *
     * @param typeEnum 促销商品类型
     * @param promotionId 促销活动id
     * @param skuId 批量商品skuId
     * @return {@link List }<{@link Integer }>
     * @since 2022-04-27 16:44:14
     */
    List<Integer> getPromotionGoodsStock(PromotionTypeEnum typeEnum, String promotionId, List<String> skuId);

    /**
     * 更新促销活动商品库存
     *
     * @param typeEnum 促销商品类型
     * @param promotionId 促销活动id
     * @param skuId 商品skuId
     * @param quantity 更新后的库存数量
     * @since 2022-04-27 16:44:14
     */
    void updatePromotionGoodsStock(PromotionTypeEnum typeEnum, String promotionId, String skuId, Integer quantity);

    /**
     * 更新促销活动商品索引
     *
     * @param promotionGoods 促销商品信息
     * @since 2022-04-27 16:44:14
     */
    void updatePromotionGoodsByPromotions(PromotionGoods promotionGoods);

    /**
     * 删除促销商品
     *
     * @param promotionId 促销活动id
     * @param skuIds skuId
     * @since 2022-04-27 16:44:14
     */
    void deletePromotionGoods(String promotionId, List<String> skuIds);

    /**
     * 删除促销促销商品
     *
     * @param promotionIds 促销活动id
     * @since 2022-04-27 16:44:14
     */
    void deletePromotionGoods(List<Long> promotionIds);

    /**
     * 根据参数删除促销商品
     *
     * @param searchParams 查询参数
     * @since 2022-04-27 16:44:14
     */
    void deletePromotionGoods(PromotionGoodsPageQuery searchParams);
}
