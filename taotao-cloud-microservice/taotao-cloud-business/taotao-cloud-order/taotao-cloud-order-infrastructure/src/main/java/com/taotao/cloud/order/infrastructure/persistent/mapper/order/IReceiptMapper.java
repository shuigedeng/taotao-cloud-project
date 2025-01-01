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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.sys.model.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.sys.model.page.order.ReceiptPageQuery;
import com.taotao.cloud.order.sys.model.vo.order.OrderSimpleVO;
import com.taotao.cloud.order.infrastructure.persistent.po.order.ReceiptPO;
import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 发票数据处理层 */
public interface IReceiptMapper extends BaseSuperMapper<ReceiptPO, Long> {

    /**
     * 查询发票信息
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     */
    @Select("select r.*,o.order_status from tt_receipt r inner join tt_order o ON o.sn=r.order_sn"
            + " ${ew.customSqlSegment}")
    IPage<OrderReceiptDTO> getReceipt(
            IPage<OrderSimpleVO> page, @Param(Constants.WRAPPER) Wrapper<ReceiptPageQuery> queryWrapper);
}
