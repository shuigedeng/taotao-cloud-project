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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.store.api.model.dto.AdminStoreApplyDTO;
import com.taotao.cloud.store.api.model.dto.CollectionDTO;
import com.taotao.cloud.store.api.model.dto.StoreBankDTO;
import com.taotao.cloud.store.api.model.dto.StoreCompanyDTO;
import com.taotao.cloud.store.api.model.dto.StoreEditDTO;
import com.taotao.cloud.store.api.model.dto.StoreOtherInfoDTO;
import com.taotao.cloud.store.api.model.query.StorePageQuery;
import com.taotao.cloud.store.api.model.vo.StoreVO;
import com.taotao.cloud.store.biz.model.entity.Store;

/**
 * 店铺业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 15:01:47
 */
public interface IStoreService extends IService<Store> {

    /**
     * 分页条件查询 用于展示店铺列表
     *
     * @param storePageQuery 存储页面查询
     * @return {@link IPage }<{@link StoreVO }>
     * @since 2022-06-01 15:01:47
     */
    IPage<StoreVO> findByConditionPage(StorePageQuery storePageQuery);

    /**
     * 获取当前登录店铺信息
     *
     * @return {@link StoreVO }
     * @since 2022-06-01 15:01:47
     */
    StoreVO getStoreDetail();

    /**
     * 增加店铺 用于后台添加店铺
     *
     * @param adminStoreApplyDTO 后台添加店铺信息
     * @return {@link Store }
     * @since 2022-06-01 15:01:47
     */
    Store add(AdminStoreApplyDTO adminStoreApplyDTO);

    /**
     * 编辑店铺
     *
     * @param storeEditDTO 店铺修改信息
     * @return {@link Store }
     * @since 2022-06-01 15:01:47
     */
    Store edit(StoreEditDTO storeEditDTO);

    /**
     * 审核店铺
     *
     * @param id 店铺ID
     * @param passed 审核结果
     * @return boolean
     * @since 2022-06-01 15:01:47
     */
    boolean audit(String id, Integer passed);

    /**
     * 关闭店铺
     *
     * @param id 店铺ID
     * @return boolean
     * @since 2022-06-01 15:01:47
     */
    boolean disable(String id);

    /**
     * 开启店铺
     *
     * @param id 店铺ID
     * @return boolean
     * @since 2022-06-01 15:01:47
     */
    boolean enable(String id);

    /**
     * 申请店铺第一步 设置店铺公司信息，如果没有店铺新建店铺
     *
     * @param storeCompanyDTO 店铺公司信息
     * @return boolean
     * @since 2022-06-01 15:01:47
     */
    boolean applyFirstStep(StoreCompanyDTO storeCompanyDTO);

    /**
     * 申请店铺第二步
     *
     * @param storeBankDTO 店铺银行信息
     * @return boolean
     * @since 2022-06-01 15:01:47
     */
    boolean applySecondStep(StoreBankDTO storeBankDTO);

    /**
     * 申请店铺第三步 设置店铺信息，经营范围
     *
     * @param storeOtherInfoDTO 店铺其他信息
     * @return boolean
     * @since 2022-06-01 15:01:47
     */
    boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO);

    /**
     * 更新店铺商品数量
     *
     * @param storeId 店铺ID
     * @since 2022-06-01 15:01:47
     */
    void updateStoreGoodsNum(Long storeId);

    /**
     * 更新店铺收藏数量
     *
     * @param collectionDTO 收藏信息
     * @since 2022-06-01 15:01:47
     */
    void updateStoreCollectionNum(CollectionDTO collectionDTO);
}
