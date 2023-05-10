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

package com.taotao.cloud.store.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.store.api.model.dto.StoreAfterSaleAddressDTO;
import com.taotao.cloud.store.api.model.dto.StoreSettingDTO;
import com.taotao.cloud.store.api.model.dto.StoreSettlementDay;
import com.taotao.cloud.store.api.model.vo.StoreBasicInfoVO;
import com.taotao.cloud.store.api.model.vo.StoreDetailInfoVO;
import com.taotao.cloud.store.api.model.vo.StoreManagementCategoryVO;
import com.taotao.cloud.store.api.model.vo.StoreOtherVO;
import com.taotao.cloud.store.biz.model.entity.Store;
import com.taotao.cloud.store.biz.model.entity.StoreDetail;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 店铺详细业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 15:00:34
 */
public interface IStoreDetailService extends IService<StoreDetail> {
    /**
     * 根据店铺ID获取店铺信息VO
     *
     * @param storeId 店铺ID
     * @return {@link StoreDetailInfoVO }
     * @since 2022-06-01 15:00:34
     */
    StoreDetailInfoVO getStoreDetailVO(String storeId);

    /**
     * 根据会员ID获取店铺信息VO
     *
     * @param memberId 会员ID
     * @return {@link StoreDetailInfoVO }
     * @since 2022-06-01 15:00:34
     */
    StoreDetailInfoVO getStoreDetailVOByMemberId(Long memberId);

    /**
     * 根据店铺ID获取店铺信息DO
     *
     * @param storeId 店铺ID
     * @return {@link StoreDetail }
     * @since 2022-06-01 15:00:34
     */
    StoreDetail getStoreDetail(Long storeId);

    /**
     * 修改商家设置
     *
     * @param storeSettingDTO 店铺设置信息
     * @return {@link Boolean }
     * @since 2022-06-01 15:00:34
     */
    Boolean editStoreSetting(StoreSettingDTO storeSettingDTO);

    /**
     * 获取店铺基本信息 用于前端店铺信息展示
     *
     * @param storeId 店铺ID
     * @return {@link StoreBasicInfoVO }
     * @since 2022-06-01 15:00:34
     */
    StoreBasicInfoVO getStoreBasicInfoDTO(String storeId);

    /**
     * 获取当前登录店铺售后收件地址
     *
     * @return {@link StoreAfterSaleAddressDTO }
     * @since 2022-06-01 15:00:34
     */
    StoreAfterSaleAddressDTO getStoreAfterSaleAddressDTO();

    /**
     * 获取某一个店铺的退货收件地址信息
     *
     * @param id 店铺ID
     * @return {@link StoreAfterSaleAddressDTO }
     * @since 2022-06-01 15:00:34
     */
    StoreAfterSaleAddressDTO getStoreAfterSaleAddressDTO(Long id);

    /**
     * 修改当前登录店铺售后收件地址
     *
     * @param storeAfterSaleAddressDTO 店铺售后DTO
     * @return boolean
     * @since 2022-06-01 15:00:34
     */
    boolean editStoreAfterSaleAddressDTO(StoreAfterSaleAddressDTO storeAfterSaleAddressDTO);

    /**
     * 修改店铺库存预警数量
     *
     * @param stockWarning 库存预警数量
     * @return boolean
     * @since 2022-06-01 15:00:34
     */
    boolean updateStockWarning(Integer stockWarning);

    /**
     * 获取店铺经营范围
     *
     * @param storeId 店铺ID
     * @return {@link List }<{@link StoreManagementCategoryVO }>
     * @since 2022-06-01 15:00:34
     */
    List<StoreManagementCategoryVO> goodsManagementCategory(String storeId);

    /**
     * 获取店铺其他信息
     *
     * @param storeId 店铺ID
     * @return {@link StoreOtherVO }
     * @since 2022-06-01 15:00:34
     */
    StoreOtherVO getStoreOtherVO(String storeId);

    /**
     * 更新店铺内所有商品信息
     *
     * @param store 店铺信息
     * @since 2022-06-01 15:00:34
     */
    void updateStoreGoodsInfo(Store store);

    /**
     * 修改店铺udesk字段设置
     *
     * @param merchantEuid 店铺客服信息
     * @return {@link Boolean }
     * @since 2022-06-01 15:00:34
     */
    Boolean editMerchantEuid(String merchantEuid);

    /**
     * 得到解决存储
     *
     * @param day 一天
     * @return {@link List }<{@link StoreSettlementDay }>
     * @since 2022-06-01 15:00:34
     */
    List<StoreSettlementDay> getSettlementStore(int day);

    /**
     * 更新交割日
     *
     * @param storeId 存储id
     * @param endTime 结束时间
     * @since 2022-06-01 15:00:35
     */
    void updateSettlementDay(Long storeId, LocalDateTime endTime);
}
