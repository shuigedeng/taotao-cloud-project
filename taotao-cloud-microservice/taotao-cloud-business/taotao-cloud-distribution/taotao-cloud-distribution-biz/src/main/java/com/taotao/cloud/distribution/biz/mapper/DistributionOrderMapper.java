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

package com.taotao.cloud.distribution.biz.mapper;

import com.taotao.cloud.distribution.biz.model.entity.DistributionOrder;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Update;

/**
 * 分销订单数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-22 11:27:46
 */
public interface DistributionOrderMapper extends MpSuperMapper<DistributionOrder, Long> {

    /**
     * 修改分销员提现金额
     *
     * @param distributionOrderStatus 分销订单状态
     * @param settleCycle 时间
     */
    @Update(
            """
		UPDATE tt_distribution AS d
		SET d.can_rebate = ( d.can_rebate + (SELECT SUM( dorder.rebate )
		 											FROM tt_distribution_order AS dorder
		 											WHERE dorder.distribution_order_status = #{distributionOrderStatus} AND dorder.settle_cycle< #{settleCycle} AND dorder.distribution_id = d.id
		 											)
		 					)
		            ,d.commission_frozen = (d.commission_frozen - (SELECT SUM( dorder.rebate )
		            												FROM tt_distribution_order AS dorder
		            												 WHERE dorder.distribution_order_status = #{distributionOrderStatus} AND dorder.settle_cycle< #{settleCycle} AND dorder.distribution_id = d.id
		            						)
		            		)
		""")
    void rebate(String distributionOrderStatus, DateTime settleCycle);
}
