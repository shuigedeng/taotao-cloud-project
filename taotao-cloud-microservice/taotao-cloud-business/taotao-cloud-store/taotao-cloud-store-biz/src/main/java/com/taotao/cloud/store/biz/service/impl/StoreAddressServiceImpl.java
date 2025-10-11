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

package com.taotao.cloud.store.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.store.biz.mapper.StoreAddressMapper;
import com.taotao.cloud.store.biz.model.entity.StoreAddress;
import com.taotao.cloud.store.biz.service.IStoreAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 店铺地址（自提点）业务层实现
 *
 * @since 2020/11/22 16:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreAddressServiceImpl extends ServiceImpl<StoreAddressMapper, StoreAddress>
        implements IStoreAddressService {

    @Override
    public IPage<StoreAddress> getStoreAddress(String storeId, PageQuery PageQuery) {
        // 获取当前登录商家账号
        LambdaQueryWrapper<StoreAddress> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StoreAddress::getStoreId, storeId);
        return this.page(PageQuery.buildMpPage(), lambdaQueryWrapper);
    }

    @Override
    public StoreAddress addStoreAddress(String storeId, StoreAddress storeAddress) {
        // 获取当前登录商家账号
        storeAddress.setStoreId(storeId);
        // 添加自提点
        this.save(storeAddress);
        return storeAddress;
    }

    @Override
    public StoreAddress editStoreAddress(String storeId, StoreAddress storeAddress) {
        // 获取当前登录商家账号
        storeAddress.setStoreId(storeId);
        // 添加自提点
        this.updateById(storeAddress);
        return storeAddress;
    }

    @Override
    public Boolean removeStoreAddress(String id) {
        return this.removeById(id);
    }
}
