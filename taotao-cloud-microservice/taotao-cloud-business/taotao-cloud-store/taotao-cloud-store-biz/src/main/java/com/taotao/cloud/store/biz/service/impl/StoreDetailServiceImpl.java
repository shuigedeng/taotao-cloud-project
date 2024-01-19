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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import com.taotao.cloud.goods.api.feign.IFeignCategoryApi;
import com.taotao.cloud.goods.api.feign.IFeignGoodsApi;
import com.taotao.cloud.goods.api.model.vo.CategoryTreeVO;
import com.taotao.cloud.store.api.model.dto.StoreAfterSaleAddressDTO;
import com.taotao.cloud.store.api.model.dto.StoreSettingDTO;
import com.taotao.cloud.store.api.model.dto.StoreSettlementDay;
import com.taotao.cloud.store.api.model.vo.StoreBasicInfoVO;
import com.taotao.cloud.store.api.model.vo.StoreDetailInfoVO;
import com.taotao.cloud.store.api.model.vo.StoreManagementCategoryVO;
import com.taotao.cloud.store.api.model.vo.StoreOtherVO;
import com.taotao.cloud.store.biz.mapper.StoreDetailMapper;
import com.taotao.cloud.store.biz.model.entity.Store;
import com.taotao.cloud.store.biz.model.entity.StoreDetail;
import com.taotao.cloud.store.biz.service.IStoreDetailService;
import com.taotao.cloud.store.biz.service.IStoreService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 店铺详细业务层实现
 *
 * @since 2020-03-07 16:18:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreDetailServiceImpl extends ServiceImpl<StoreDetailMapper, StoreDetail> implements IStoreDetailService {

    /** 店铺 */
    @Autowired
    private IStoreService storeService;
    /** 分类 */
    @Autowired
    private IFeignCategoryApi categoryApi;

    @Autowired
    private IFeignGoodsApi goodsApi;

    // @Autowired
    // private RocketmqCustomProperties rocketmqCustomProperties;
	//
    // @Autowired
    // private RocketMQTemplate rocketMQTemplate;

    @Override
    public StoreDetailInfoVO getStoreDetailVO(String storeId) {
        return this.baseMapper.getStoreDetail(storeId);
    }

    @Override
    public StoreDetailInfoVO getStoreDetailVOByMemberId(Long memberId) {
        return this.baseMapper.getStoreDetailByMemberId(memberId);
    }

    @Override
    public StoreDetail getStoreDetail(Long storeId) {
        LambdaQueryWrapper<StoreDetail> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StoreDetail::getStoreId, storeId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public Boolean editStoreSetting(StoreSettingDTO storeSettingDTO) {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        // 修改店铺
        Store store = storeService.getById(storeId);
        BeanUtils.copyProperties(storeSettingDTO, store);
        boolean result = storeService.updateById(store);
        if (result) {
            this.updateStoreGoodsInfo(store);
        }
        return result;
    }

    @Override
    public void updateStoreGoodsInfo(Store store) {
        goodsApi.updateStoreDetail(store.getId());

        // Map<String, Object> updateIndexFieldsMap = EsIndexUtil.getUpdateIndexFieldsMap(
        //         MapUtil.builder().put("storeId", store.getId()).build(),
        //         MapUtil.builder()
        //                 .put("storeName", store.getStoreName())
        //                 .put("selfOperated", store.getSelfOperated())
        //                 .build());
        // String destination =
        //         rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.UPDATE_GOODS_INDEX_FIELD.name();
        // // 发送mq消息
        // rocketMQTemplate.asyncSend(
        //         destination, JSONUtil.toJsonStr(updateIndexFieldsMap), RocketmqSendCallbackBuilder.commonCallback());
    }

    @Override
    public Boolean editMerchantEuid(String merchantEuid) {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        Store store = storeService.getById(storeId);
        store.setMerchantEuid(merchantEuid);
        return storeService.updateById(store);
    }

    @Override
    public List<StoreSettlementDay> getSettlementStore(int day) {
        return null;
    }

    @Override
    public void updateSettlementDay(Long storeId, LocalDateTime endTime) {}

    @Override
    public StoreBasicInfoVO getStoreBasicInfoDTO(String storeId) {
        return this.baseMapper.getStoreBasicInfoDTO(storeId);
    }

    @Override
    public StoreAfterSaleAddressDTO getStoreAfterSaleAddressDTO() {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        return this.baseMapper.getStoreAfterSaleAddressDTO(storeId);
    }

    @Override
    public StoreAfterSaleAddressDTO getStoreAfterSaleAddressDTO(Long id) {
        StoreAfterSaleAddressDTO storeAfterSaleAddressDTO = this.baseMapper.getStoreAfterSaleAddressDTO(id);
        if (storeAfterSaleAddressDTO == null) {
            storeAfterSaleAddressDTO = new StoreAfterSaleAddressDTO();
        }
        return storeAfterSaleAddressDTO;
    }

    @Override
    public boolean editStoreAfterSaleAddressDTO(StoreAfterSaleAddressDTO storeAfterSaleAddressDTO) {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        LambdaUpdateWrapper<StoreDetail> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeName, storeAfterSaleAddressDTO.getSalesConsigneeName());
        lambdaUpdateWrapper.set(
                StoreDetail::getSalesConsigneeAddressId, storeAfterSaleAddressDTO.getSalesConsigneeAddressId());
        lambdaUpdateWrapper.set(
                StoreDetail::getSalesConsigneeAddressPath, storeAfterSaleAddressDTO.getSalesConsigneeAddressPath());
        lambdaUpdateWrapper.set(
                StoreDetail::getSalesConsigneeDetail, storeAfterSaleAddressDTO.getSalesConsigneeDetail());
        lambdaUpdateWrapper.set(
                StoreDetail::getSalesConsigneeMobile, storeAfterSaleAddressDTO.getSalesConsigneeMobile());
        lambdaUpdateWrapper.eq(StoreDetail::getStoreId, storeId);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean updateStockWarning(Integer stockWarning) {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        LambdaUpdateWrapper<StoreDetail> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(StoreDetail::getStockWarning, stockWarning);
        lambdaUpdateWrapper.eq(StoreDetail::getStoreId, storeId);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public List<StoreManagementCategoryVO> goodsManagementCategory(String storeId) {

        // 获取顶部分类列表
        List<CategoryTreeVO> categoryList = categoryApi.firstCategory();
        // 获取店铺信息
        StoreDetail storeDetail =
                this.getOne(new LambdaQueryWrapper<StoreDetail>().eq(StoreDetail::getStoreId, storeId));
        // 获取店铺分类
        String[] storeCategoryList = storeDetail.getGoodsManagementCategory().split(",");
        List<StoreManagementCategoryVO> list = new ArrayList<>();
        for (CategoryTreeVO category : categoryList) {
            StoreManagementCategoryVO storeManagementCategoryVO = new StoreManagementCategoryVO();
            BeanUtils.copyProperties(category, storeManagementCategoryVO);

            for (String storeCategory : storeCategoryList) {
                if (Long.valueOf(storeCategory).equals(category.getId())) {
                    storeManagementCategoryVO.setSelected(true);
                }
            }
            list.add(storeManagementCategoryVO);
        }
        return list;
    }

    @Override
    public StoreOtherVO getStoreOtherVO(String storeId) {
        return this.baseMapper.getLicencePhoto(storeId);
    }
}
