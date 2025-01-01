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
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderItemPO;
import com.taotao.cloud.order.sys.model.vo.order.OrderSimpleVO;
import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 子订单数据处理层 */
public interface IOrderItemMapper extends BaseSuperMapper<OrderItemPO, Long> {

    /**
     * 获取等待操作订单子项目
     *
     * @param queryWrapper 查询条件
     * @return 订单子项列表
     */
    @Select(
            """
		SELECT *
		FROM tt_order_item AS oi INNER JOIN tt_order AS o ON oi.order_sn=o.sn
		${ew.customSqlSegment}
		""")
    List<OrderItemPO> waitOperationOrderItem(@Param(Constants.WRAPPER) Wrapper<OrderSimpleVO> queryWrapper);
}
