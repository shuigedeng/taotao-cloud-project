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
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.biz.model.dto.GoodsOperationDTO;
import com.taotao.cloud.goods.biz.model.page.GoodsPageQuery;
import com.taotao.cloud.goods.biz.model.vo.GoodsSkuParamsVO;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.boot.webagg.service.BaseSuperService;
import java.util.List;

/**
 * 商品业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:00:15
 */
public interface GoodsService extends BaseSuperService<Goods, Long> {

    /**
     * 根据品牌获取商品
     *
     * @param brandIds 品牌ids
     * @return {@link List }<{@link Goods }>
     * @since 2022-04-27 17:00:15
     */
    List<Goods> getByBrandIds(List<Long> brandIds);

    /**
     * 下架所有商家商品
     *
     * @param storeId 店铺ID
     * @return {@link boolean }
     * @since 2022-04-27 17:00:15
     */
    boolean underStoreGoods(Long storeId);

    /**
     * 更新商品参数
     *
     * @param goodsId 商品id
     * @param params 商品参数
     * @return {@link boolean }
     * @since 2022-04-27 17:00:15
     */
    boolean updateGoodsParams(Long goodsId, String params);

    /**
     * 获取某分类下的商品数量
     *
     * @param categoryId 分类ID
     * @return {@link Long }
     * @since 2022-04-27 17:00:15
     */
    Long getGoodsCountByCategory(Long categoryId);

    /**
     * 添加商品
     *
     * @param goodsOperationDTO 商品查询条件
     * @return {@link boolean }
     * @since 2022-04-27 17:00:15
     */
    boolean addGoods(GoodsOperationDTO goodsOperationDTO);

    /**
     * 修改商品
     *
     * @param goodsOperationDTO 商品查询条件
     * @param goodsId 商品ID
     * @return {@link boolean }
     * @since 2022-04-27 17:00:15
     */
    boolean editGoods(GoodsOperationDTO goodsOperationDTO, Long goodsId);

    /**
     * 查询商品VO
     *
     * @param goodsId 商品id
     * @return {@link GoodsSkuParamsVO }
     * @since 2022-04-27 17:00:16
     */
    GoodsSkuParamsVO getGoodsVO(Long goodsId);

    /**
     * 商品查询
     *
     * @param goodsPageQuery 查询参数
     * @return {@link IPage }<{@link Goods }>
     * @since 2022-04-27 17:00:16
     */
    IPage<Goods> goodsQueryPage(GoodsPageQuery goodsPageQuery);

    /**
     * 商品查询
     *
     * @param goodsPageQuery 查询参数
     * @return {@link List }<{@link Goods }>
     * @since 2022-04-27 17:00:16
     */
    List<Goods> queryListByParams(GoodsPageQuery goodsPageQuery);

    /**
     * 批量审核商品
     *
     * @param goodsIds 商品id列表
     * @param goodsAuthEnum 审核操作
     * @return {@link boolean }
     * @since 2022-04-27 17:00:16
     */
    boolean auditGoods(List<Long> goodsIds, GoodsAuthEnum goodsAuthEnum);

    /**
     * 更新商品上架状态状态
     *
     * @param goodsIds 商品ID集合
     * @param goodsStatusEnum 更新的商品状态
     * @param underReason 下架原因
     * @return {@link boolean }
     * @since 2022-04-27 17:00:16
     */
    boolean updateGoodsMarketAble(List<Long> goodsIds, GoodsStatusEnum goodsStatusEnum, String underReason);

    /**
     * 更新商品上架状态状态
     *
     * @param goodsIds 商品ID集合
     * @param goodsStatusEnum 更新的商品状态
     * @param underReason 下架原因
     * @return {@link boolean }
     * @since 2022-04-27 17:00:16
     */
    boolean managerUpdateGoodsMarketAble(List<Long> goodsIds, GoodsStatusEnum goodsStatusEnum, String underReason);

    /**
     * 删除商品
     *
     * @param goodsIds 商品ID
     * @return {@link boolean }
     * @since 2022-04-27 17:00:16
     */
    boolean deleteGoods(List<Long> goodsIds);

    /**
     * 设置商品运费模板
     *
     * @param goodsIds 商品列表
     * @param templateId 运费模板ID
     * @return {@link boolean }
     * @since 2022-04-27 17:00:16
     */
    boolean freight(List<Long> goodsIds, Long templateId);

    /**
     * 修改商品库存数量
     *
     * @param goodsId 商品ID
     * @param quantity 库存数量
     * @return {@link boolean }
     * @since 2022-04-27 17:00:16
     */
    boolean updateStock(Long goodsId, Integer quantity);

    /**
     * 更新商品评价数量
     *
     * @param goodsId 商品ID
     * @return {@link boolean }
     * @since 2022-04-27 17:00:16
     */
    boolean updateGoodsCommentNum(Long goodsId);

    /**
     * 更新商品的购买数量
     *
     * @param goodsId 商品ID
     * @param buyCount 购买数量
     * @return {@link boolean }
     * @since 2022-04-27 17:00:16
     */
    boolean updateGoodsBuyCount(Long goodsId, int buyCount);

    /**
     * 批量更新商品的店铺信息
     *
     * @param store
     */
    // boolean updateStoreDetail(Store store);

    /**
     * 统计店铺的商品数量
     *
     * @param storeId 店铺id
     * @return {@link Long }
     * @since 2022-04-27 17:00:16
     */
    Long countStoreGoodsNum(Long storeId);
}
