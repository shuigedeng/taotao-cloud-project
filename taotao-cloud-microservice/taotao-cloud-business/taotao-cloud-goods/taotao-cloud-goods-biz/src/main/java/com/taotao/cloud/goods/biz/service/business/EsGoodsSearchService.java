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

import com.taotao.cloud.goods.biz.model.dto.HotWordsDTO;
import com.taotao.cloud.goods.biz.model.page.EsGoodsSearchQuery;
import com.taotao.cloud.goods.biz.elasticsearch.entity.EsGoodsIndex;
import com.taotao.cloud.goods.biz.elasticsearch.pojo.EsGoodsRelatedInfo;
import java.util.List;
import org.springframework.data.elasticsearch.core.SearchPage;

/**
 * ES商品搜索业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:00:07
 */
public interface EsGoodsSearchService {

    /**
     * 商品搜索
     *
     * @param esGoodsSearchQuery 搜索参数
     * @return {@link SearchPage }<{@link EsGoodsIndex }>
     * @since 2022-04-27 17:00:07
     */
    SearchPage<EsGoodsIndex> searchGoods(EsGoodsSearchQuery esGoodsSearchQuery);

    /**
     * 获取热门关键词
     *
     * @param count 热词数量
     * @return {@link List }<{@link String }>
     * @since 2022-04-27 17:00:07
     */
    List<String> getHotWords(Integer count);

    /**
     * 设置热门关键词
     *
     * @param hotWords 热词分数
     * @return {@link boolean }
     * @since 2022-04-27 17:00:07
     */
    boolean setHotWords(HotWordsDTO hotWords);

    /**
     * 删除热门关键词
     *
     * @param keywords 热词
     * @return {@link boolean }
     * @since 2022-04-27 17:00:07
     */
    boolean deleteHotWords(String keywords);

    /**
     * 获取筛选器
     *
     * @param esGoodsSearchQuery 搜索条件
     * @return {@link EsGoodsRelatedInfo }
     * @since 2022-04-27 17:00:07
     */
    EsGoodsRelatedInfo getSelector(EsGoodsSearchQuery esGoodsSearchQuery);

    /**
     * 根据SkuID列表获取ES商品
     *
     * @param skuIds SkuId列表
     * @return {@link List }<{@link EsGoodsIndex }>
     * @since 2022-04-27 17:00:07
     */
    List<EsGoodsIndex> getEsGoodsBySkuIds(List<Long> skuIds);

    /**
     * 根据id获取商品索引
     *
     * @param id 商品skuId
     * @return {@link EsGoodsIndex }
     * @since 2022-04-27 17:00:07
     */
    EsGoodsIndex getEsGoodsById(Long id);
}
