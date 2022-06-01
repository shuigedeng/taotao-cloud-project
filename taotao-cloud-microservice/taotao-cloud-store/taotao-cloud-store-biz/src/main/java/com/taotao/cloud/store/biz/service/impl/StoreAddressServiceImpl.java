package com.taotao.cloud.store.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.store.biz.entity.StoreAddress;
import com.taotao.cloud.store.biz.mapper.StoreAddressMapper;
import com.taotao.cloud.store.biz.service.StoreAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 店铺地址（自提点）业务层实现
 *
 * 
 * @since 2020/11/22 16:00
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreAddressServiceImpl extends ServiceImpl<StoreAddressMapper, StoreAddress> implements
	StoreAddressService {

    @Override
    public IPage<StoreAddress> getStoreAddress(String storeId, PageParam pageParam) {
        //获取当前登录商家账号
        LambdaQueryWrapper<StoreAddress> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StoreAddress::getStoreId, storeId);
        return this.page(pageParam.buildMpPage(), lambdaQueryWrapper);
    }

    @Override
    public StoreAddress addStoreAddress(String storeId, StoreAddress storeAddress) {
        //获取当前登录商家账号
        storeAddress.setStoreId(storeId);
        //添加自提点
        this.save(storeAddress);
        return storeAddress;
    }

    @Override
    public StoreAddress editStoreAddress(String storeId, StoreAddress storeAddress) {
        //获取当前登录商家账号
        storeAddress.setStoreId(storeId);
        //添加自提点
        this.updateById(storeAddress);
        return storeAddress;
    }

    @Override
    public Boolean removeStoreAddress(String id) {
        return this.removeById(id);
    }
}
