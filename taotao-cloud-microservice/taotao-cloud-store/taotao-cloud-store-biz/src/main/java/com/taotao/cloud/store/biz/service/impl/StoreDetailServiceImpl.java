package com.taotao.cloud.store.biz.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.store.api.dto.StoreAfterSaleAddressDTO;
import com.taotao.cloud.store.api.dto.StoreSettingDTO;
import com.taotao.cloud.store.api.vo.StoreBasicInfoVO;
import com.taotao.cloud.store.api.vo.StoreDetailVO;
import com.taotao.cloud.store.api.vo.StoreManagementCategoryVO;
import com.taotao.cloud.store.api.vo.StoreOtherVO;
import com.taotao.cloud.store.biz.entity.Store;
import com.taotao.cloud.store.biz.entity.StoreDetail;
import com.taotao.cloud.store.biz.mapper.StoreDetailMapper;
import com.taotao.cloud.store.biz.service.StoreDetailService;
import com.taotao.cloud.store.biz.service.StoreService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 店铺详细业务层实现
 *
 * @author pikachu
 * @since 2020-03-07 16:18:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreDetailServiceImpl extends ServiceImpl<StoreDetailMapper, StoreDetail> implements
	StoreDetailService {

    /**
     * 店铺
     */
    @Autowired
    private StoreService storeService;
    /**
     * 分类
     */
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public StoreDetailVO getStoreDetailVO(String storeId) {
        return this.baseMapper.getStoreDetail(storeId);
    }

    @Override
    public StoreDetailVO getStoreDetailVOByMemberId(String memberId) {
        return this.baseMapper.getStoreDetailByMemberId(memberId);
    }

    @Override
    public StoreDetail getStoreDetail(String storeId) {
        LambdaQueryWrapper<StoreDetail> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StoreDetail::getStoreId, storeId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public Boolean editStoreSetting(StoreSettingDTO storeSettingDTO) {
        AuthUser tokenUser = Objects.requireNonNull(UserContext.getCurrentUser());
        //修改店铺
        Store store = storeService.getById(tokenUser.getStoreId());
        BeanUtil.copyProperties(storeSettingDTO, store);
        boolean result = storeService.updateById(store);
        if (result) {
            this.updateStoreGoodsInfo(store);
        }
        return result;
    }

    @Override
    public void updateStoreGoodsInfo(Store store) {

        goodsService.updateStoreDetail(store);

        Map<String, Object> updateIndexFieldsMap = EsIndexUtil.getUpdateIndexFieldsMap(
                MapUtil.builder().put("storeId", store.getId()).build(),
                MapUtil.builder().put("storeName", store.getStoreName()).put("selfOperated", store.getSelfOperated()).build());
        String destination = rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.UPDATE_GOODS_INDEX_FIELD.name();
        //发送mq消息
        rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(updateIndexFieldsMap), RocketmqSendCallbackBuilder.commonCallback());
    }

    @Override
    public Boolean editMerchantEuid(String merchantEuid) {
        AuthUser tokenUser = Objects.requireNonNull(UserContext.getCurrentUser());
        Store store = storeService.getById(tokenUser.getStoreId());
        store.setMerchantEuid(merchantEuid);
        return storeService.updateById(store);
    }

    @Override
    public StoreBasicInfoVO getStoreBasicInfoDTO(String storeId) {
        return this.baseMapper.getStoreBasicInfoDTO(storeId);
    }

    @Override
    public StoreAfterSaleAddressDTO getStoreAfterSaleAddressDTO() {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        return this.baseMapper.getStoreAfterSaleAddressDTO(storeId);
    }

    @Override
    public StoreAfterSaleAddressDTO getStoreAfterSaleAddressDTO(String id) {
        StoreAfterSaleAddressDTO storeAfterSaleAddressDTO = this.baseMapper.getStoreAfterSaleAddressDTO(id);
        if (storeAfterSaleAddressDTO == null) {
            storeAfterSaleAddressDTO = new StoreAfterSaleAddressDTO();
        }
        return storeAfterSaleAddressDTO;
    }

    @Override
    public boolean editStoreAfterSaleAddressDTO(StoreAfterSaleAddressDTO storeAfterSaleAddressDTO) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        LambdaUpdateWrapper<StoreDetail> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeName, storeAfterSaleAddressDTO.getSalesConsigneeName());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeAddressId, storeAfterSaleAddressDTO.getSalesConsigneeAddressId());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeAddressPath, storeAfterSaleAddressDTO.getSalesConsigneeAddressPath());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeDetail, storeAfterSaleAddressDTO.getSalesConsigneeDetail());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeMobile, storeAfterSaleAddressDTO.getSalesConsigneeMobile());
        lambdaUpdateWrapper.eq(StoreDetail::getStoreId, storeId);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean updateStockWarning(Integer stockWarning) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        LambdaUpdateWrapper<StoreDetail> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(StoreDetail::getStockWarning, stockWarning);
        lambdaUpdateWrapper.eq(StoreDetail::getStoreId, storeId);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public List<StoreManagementCategoryVO> goodsManagementCategory(String storeId) {

        //获取顶部分类列表
        List<Category> categoryList = categoryService.firstCategory();
        //获取店铺信息
        StoreDetail storeDetail = this.getOne(new LambdaQueryWrapper<StoreDetail>().eq(StoreDetail::getStoreId, storeId));
        //获取店铺分类
        String[] storeCategoryList = storeDetail.getGoodsManagementCategory().split(",");
        List<StoreManagementCategoryVO> list = new ArrayList<>();
        for (Category category : categoryList) {
            StoreManagementCategoryVO storeManagementCategoryVO = new StoreManagementCategoryVO(category);
            for (String storeCategory : storeCategoryList) {
                if (storeCategory.equals(category.getId())) {
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
