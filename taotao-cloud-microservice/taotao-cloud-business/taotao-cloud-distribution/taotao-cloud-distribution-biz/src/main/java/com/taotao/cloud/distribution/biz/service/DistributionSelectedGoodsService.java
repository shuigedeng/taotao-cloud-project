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

package com.taotao.cloud.distribution.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.distribution.biz.model.entity.DistributionSelectedGoods;

/** 分销选择商品业务层 */
public interface DistributionSelectedGoodsService extends IService<DistributionSelectedGoods> {

    /**
     * 分销员添加分销商品
     *
     * @param distributionGoodsId 分销商品ID
     * @return 是否添加成功
     */
    boolean add(String distributionGoodsId);

    /**
     * 分销员删除分销商品
     *
     * @param distributionGoodsId 分销商品ID
     * @return 是否删除成功
     */
    boolean delete(String distributionGoodsId);

    /**
     * 分销员删除分销商品（管理员操作）
     *
     * @param distributionGoodsId 分销商品ID
     * @return 是否删除成功
     */
    boolean deleteByDistributionGoodsId(String distributionGoodsId);
}
