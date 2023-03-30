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

import java.util.Map;

/**
 * 促销业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:19
 */
public interface IPromotionService {

    /**
     * 获取当前进行的所有促销活动信息
     *
     * @return {@link Map }<{@link String }, {@link Object }>
     * @since 2022-04-27 16:44:19
     */
    Map<String, Object> getCurrentPromotion();

    /**
     * 根据商品索引获取当前商品索引的所有促销活动信息
     *
     * @param index 商品索引
     * @return {@link Map }<{@link String }, {@link Object }>
     * @since 2022-04-27 16:44:19
     */
    Map<String, Object> getGoodsCurrentPromotionMap(EsGoodsIndex index);
}
