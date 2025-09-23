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

package com.taotao.cloud.store.biz.mapper;

import com.taotao.cloud.store.api.model.vo.StoreLogisticsVO;
import com.taotao.cloud.store.biz.model.entity.StoreLogistics;
import java.util.List;

import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Select;

/** 物流公司数据处理层 */
public interface StoreLogisticsMapper extends MpSuperMapper<StoreLogistics, String> {

    /**
     * 获取店铺选择的物流公司
     *
     * @param storeId 店铺ID
     * @return 物流公司列表
     */
    @Select("SELECT l.* FROM tt_logistics l RIGHT JOIN  tt_store_logistics sl ON"
            + " l.id=sl.logistics_id WHERE sl.store_id=#{storeId} AND l.disabled='OPEN'")
    List<StoreLogisticsVO> getSelectedStoreLogistics(String storeId);

    /**
     * 店铺已选择的物流公司名称列表
     *
     * @param storeId 店铺ID
     * @return 店铺已选择的物流公司名称列表
     */
    @Select("SELECT l.name FROM tt_logistics l RIGHT JOIN  tt_store_logistics sl ON"
            + " l.id=sl.logistics_id WHERE sl.store_id=#{storeId} AND l.disabled='OPEN'")
    List<String> getSelectedStoreLogisticsName(String storeId);

    /**
     * 获取店铺地址VO列表
     *
     * @param storeId 店铺列表
     * @return 店铺地址VO列表
     */
    @Select("SELECT *, ( SELECT sl.id FROM tt_store_logistics sl WHERE l.id = sl.logistics_id AND"
            + " sl.store_id=#{storeId} ) AS selected FROM tt_logistics l WHERE"
            + " l.disabled='OPEN';")
    List<StoreLogisticsVO> getStoreLogistics(String storeId);
}
