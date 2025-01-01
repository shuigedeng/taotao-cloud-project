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

package com.taotao.cloud.order.infrastructure.persistent.mapper.order;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.infrastructure.persistent.po.order.StoreFlowPO;
import com.taotao.cloud.store.api.model.vo.StoreFlowPayDownloadVO;
import com.taotao.cloud.store.api.model.vo.StoreFlowRefundDownloadVO;
import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 商家订单流水数据处理层 */
public interface IStoreFlowMapper extends BaseSuperMapper<StoreFlowPO, Long> {

    /**
     * 获取结算单的入账流水
     *
     * @param queryWrapper 查询条件
     * @return 入账流水
     */
    @Select("SELECT * FROM tt_store_flow ${ew.customSqlSegment}")
    List<StoreFlowPayDownloadVO> getStoreFlowPayDownloadVO(@Param(Constants.WRAPPER) Wrapper<StoreFlowPO> queryWrapper);

    /**
     * 获取结算单的退款流水
     *
     * @param queryWrapper 查询条件
     * @return 退款流水
     */
    @Select("SELECT * FROM tt_store_flow ${ew.customSqlSegment}")
    List<StoreFlowRefundDownloadVO> getStoreFlowRefundDownloadVO(
            @Param(Constants.WRAPPER) Wrapper<StoreFlowPO> queryWrapper);
}
