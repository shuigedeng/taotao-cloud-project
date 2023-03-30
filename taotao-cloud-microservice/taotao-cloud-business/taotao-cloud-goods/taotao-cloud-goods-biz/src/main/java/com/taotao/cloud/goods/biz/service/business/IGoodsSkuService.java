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

package com.taotao.cloud.goods.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.goods.api.model.dto.GoodsSkuStockDTO;
import com.taotao.cloud.goods.api.model.page.GoodsPageQuery;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;
import java.util.Map;

/**
 * 商品sku业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:00:44
 */
public interface IGoodsSkuService extends BaseSuperService<GoodsSku, Long> {

    /**
     * 获取商品SKU缓存ID
     *
     * @param id SkuId
     * @return {@link String }
     * @since 2022-04-27 17:00:44
     */
    static String getCacheKeys(Long id) {
        return CachePrefix.GOODS_SKU.getPrefix() + id;
    }

    /**
     * 获取商品SKU库存缓存ID
     *
     * @param id SkuId
     * @return {@link String }
     * @since 2022-04-27 17:00:44
     */
    static String getStockCacheKey(Long id) {
        return CachePrefix.SKU_STOCK.getPrefix() + id;
    }

    /**
     * 添加商品sku
     *
     * @param skuList sku列表
     * @param goods 商品信息
     * @return {@link Boolean }
     * @since 2022-04-27 17:00:44
     */
    Boolean add(List<Map<String, Object>> skuList, Goods goods);

    /**
     * 更新商品sku
     *
     * @param skuList sku列表
     * @param goods 商品信息
     * @param regeneratorSkuFlag 是否是否重新生成sku
     * @return {@link Boolean }
     * @since 2022-04-27 17:00:44
     */
    Boolean update(List<Map<String, Object>> skuList, Goods goods, Boolean regeneratorSkuFlag);

    /**
     * 更新商品sku
     *
     * @param goodsSku sku信息
     * @return {@link Boolean }
     * @since 2022-04-27 17:00:44
     */
    Boolean update(GoodsSku goodsSku);

    /**
     * 清除sku缓存
     *
     * @param skuId skuid
     * @return {@link Boolean }
     * @since 2022-04-27 17:00:44
     */
    Boolean clearCache(Long skuId);

    /**
     * 从redis缓存中获取商品SKU信息
     *
     * @param skuId SkuId
     * @return {@link GoodsSku }
     * @since 2022-04-27 17:00:44
     */
    GoodsSku getGoodsSkuByIdFromCache(Long skuId);

    /**
     * 获取商品sku详情
     *
     * @param goodsId 商品ID
     * @param skuId skuID
     * @return {@link Map }<{@link String }, {@link Object }>
     * @since 2022-04-27 17:00:44
     */
    Map<String, Object> getGoodsSkuDetail(Long goodsId, Long skuId);

    /**
     * 批量从redis中获取商品SKU信息
     *
     * @param ids SkuId集合
     * @return {@link List }<{@link GoodsSku }>
     * @since 2022-04-27 17:00:44
     */
    List<GoodsSku> getGoodsSkuByIdFromCache(List<Long> ids);

    /**
     * 获取goodsId下所有的goodsSku
     *
     * @param goodsId 商品id
     * @return {@link List }<{@link GoodsSkuSpecGalleryVO }>
     * @since 2022-04-27 17:00:44
     */
    List<GoodsSkuSpecGalleryVO> getGoodsListByGoodsId(Long goodsId);

    /**
     * 获取goodsId下所有的goodsSku
     *
     * @param goodsId 商品id
     * @return {@link List }<{@link GoodsSku }>
     * @since 2022-04-27 17:00:44
     */
    List<GoodsSku> getGoodsSkuListByGoodsId(Long goodsId);

    /**
     * 根据goodsSku组装goodsSkuVO
     *
     * @param list 商品id
     * @return {@link List }<{@link GoodsSkuSpecGalleryVO }>
     * @since 2022-04-27 17:00:44
     */
    List<GoodsSkuSpecGalleryVO> getGoodsSkuVOList(List<GoodsSku> list);

    /**
     * 根据goodsSku组装goodsSkuVO
     *
     * @param goodsSku 商品规格
     * @return {@link GoodsSkuSpecGalleryVO }
     * @since 2022-04-27 17:00:44
     */
    GoodsSkuSpecGalleryVO getGoodsSkuVO(GoodsSku goodsSku);

    /**
     * 分页查询商品sku信息
     *
     * @param searchParams 查询参数
     * @return {@link IPage }<{@link GoodsSku }>
     * @since 2022-04-27 17:00:44
     */
    IPage<GoodsSku> goodsSkuQueryPage(GoodsPageQuery searchParams);

    /**
     * 列表查询商品sku信息
     *
     * @param searchParams 查询参数
     * @return {@link List }<{@link GoodsSku }>
     * @since 2022-04-27 17:00:44
     */
    List<GoodsSku> getGoodsSkuByList(GoodsPageQuery searchParams);

    /**
     * 更新商品sku状态
     *
     * @param goods 商品信息(Id,MarketEnable/AuthFlag)
     * @return {@link Boolean }
     * @since 2022-04-27 17:00:44
     */
    Boolean updateGoodsSkuStatus(Goods goods);

    /**
     * 发送生成ES商品索引
     *
     * @param goods 商品信息
     * @since 2022-04-27 17:00:44
     */
    void generateEs(Goods goods);

    /**
     * 更新SKU库存
     *
     * @param goodsSkuStockDTOS sku库存修改实体
     * @return {@link Boolean }
     * @since 2022-04-27 17:00:44
     */
    Boolean updateStocks(List<GoodsSkuStockDTO> goodsSkuStockDTOS);

    /**
     * 更新SKU库存
     *
     * @param skuId SKUId
     * @param quantity 设置的库存数量
     * @return {@link Boolean }
     * @since 2022-04-27 17:00:44
     */
    Boolean updateStock(Long skuId, Integer quantity);

    /**
     * 获取商品sku库存
     *
     * @param skuId 商品skuId
     * @return {@link Integer }
     * @since 2022-04-27 17:00:44
     */
    Integer getStock(Long skuId);

    /**
     * 修改商品库存字段
     *
     * @param goodsSkus
     */
    Boolean updateGoodsStuck(List<GoodsSku> goodsSkus);

    /**
     * 更新SKU评价数量
     *
     * @param skuId SKUId
     */
    Boolean updateGoodsSkuCommentNum(Long skuId);

    /**
     * 根据商品id获取全部skuId的集合
     *
     * @param goodsId goodsId
     * @return 全部skuId的集合
     */
    List<String> getSkuIdsByGoodsId(Long goodsId);
}
