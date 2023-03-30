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

import com.taotao.cloud.promotion.api.model.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.biz.model.entity.PointsGoods;
import java.util.List;

/**
 * 积分商品业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:10
 */
public interface IPointsGoodsService extends AbstractPromotionsService<PointsGoods> {

    /**
     * 批量保存库存商品
     *
     * @param promotionsList 积分商品列表
     * @return boolean
     * @since 2022-04-27 16:44:10
     */
    boolean savePointsGoodsBatch(List<PointsGoods> promotionsList);

    /**
     * 根据ID获取积分详情
     *
     * @param id 积分商品id
     * @return {@link PointsGoodsVO }
     * @since 2022-04-27 16:44:10
     */
    PointsGoodsVO getPointsGoodsDetail(String id);

    /**
     * 根据ID获取积分详情
     *
     * @param skuId 商品SkuId
     * @return {@link PointsGoodsVO }
     * @since 2022-04-27 16:44:10
     */
    PointsGoodsVO getPointsGoodsDetailBySkuId(String skuId);
}
