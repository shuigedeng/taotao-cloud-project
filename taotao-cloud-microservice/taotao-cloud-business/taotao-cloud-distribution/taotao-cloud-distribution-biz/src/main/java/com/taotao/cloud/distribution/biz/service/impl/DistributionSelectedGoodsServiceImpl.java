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

package com.taotao.cloud.distribution.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.distribution.biz.mapper.DistributionSelectedGoodsMapper;
import com.taotao.cloud.distribution.biz.model.entity.DistributionSelectedGoods;
import com.taotao.cloud.distribution.biz.service.DistributionSelectedGoodsService;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 分销选择商品接口实现 */
@Service
public class DistributionSelectedGoodsServiceImpl
        extends ServiceImpl<DistributionSelectedGoodsMapper, DistributionSelectedGoods>
        implements DistributionSelectedGoodsService {

    /** 分销员 */
    @Autowired
    private DistributionService distributionService;

    @Override
    public boolean add(String distributionGoodsId) {
        // 检查分销功能开关
        distributionService.checkDistributionSetting();

        String distributionId = distributionService.getDistribution().getId();
        DistributionSelectedGoods distributionSelectedGoods =
                new DistributionSelectedGoods(distributionId, distributionGoodsId);
        return this.save(distributionSelectedGoods);
    }

    @Override
    public boolean delete(String distributionGoodsId) {
        // 检查分销功能开关
        distributionService.checkDistributionSetting();

        String distributionId = distributionService.getDistribution().getId();
        return this.remove(new LambdaQueryWrapper<DistributionSelectedGoods>()
                .eq(DistributionSelectedGoods::getDistributionGoodsId, distributionGoodsId)
                .eq(DistributionSelectedGoods::getDistributionId, distributionId));
    }

    @Override
    public boolean deleteByDistributionGoodsId(String distributionGoodsId) {
        return this.remove(new LambdaQueryWrapper<DistributionSelectedGoods>()
                .eq(DistributionSelectedGoods::getDistributionGoodsId, distributionGoodsId));
    }
}
