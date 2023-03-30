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
import com.taotao.cloud.store.api.model.vo.StoreLogisticsVO;
import com.taotao.cloud.store.biz.model.entity.StoreLogistics;
import java.util.List;

/** 店铺-物流公司业务层 */
public interface IStoreLogisticsService extends IService<StoreLogistics> {

    /**
     * 获取当前店铺的物流公司列表
     *
     * @param storeId 店铺id
     * @return 物流公司列表
     */
    List<StoreLogisticsVO> getStoreLogistics(String storeId);

    /**
     * 获取当前店铺已选择的物流公司列表
     *
     * @param storeId 店铺id
     * @return 物流公司列表
     */
    List<StoreLogisticsVO> getStoreSelectedLogistics(String storeId);

    /**
     * 获取当前店铺已选择的物流公司名称列表
     *
     * @param storeId 店铺id
     * @return 物流公司列表
     */
    List<String> getStoreSelectedLogisticsName(String storeId);

    /**
     * 添加店铺-物流公司
     *
     * @param logisticsId 物流公司设置id
     * @param storeId 店铺id
     * @return 店铺物流公司
     */
    StoreLogistics add(String logisticsId, String storeId);
}
