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

import com.taotao.cloud.distribution.biz.model.entity.Distribution;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Update;

/** 分销员数据处理层 */
public interface DistributionMapper extends MpSuperMapper<Distribution, Long> {

    /**
     * 修改分销员可提现金额
     *
     * @param commissionFrozen 分销金额
     * @param distributionId 分销员ID
     */
    @Update(
            """
		UPDATE tt_distribution set commission_frozen = (commission_frozen+#{commissionFrozen}) , rebate_total=(rebate_total+#{commissionFrozen})
		WHERE id = #{distributionId}
		""")
    void subCanRebate(BigDecimal commissionFrozen, String distributionId);

    /**
     * 添加分销金额
     *
     * @param commissionFrozen 分销金额
     * @param distributionId 分销员ID
     */
    @Update(
            """
		UPDATE tt_distribution set commission_frozen = (commission_frozen+#{commissionFrozen})
				, rebate_total=(rebate_total+#{commissionFrozen})
				, distribution_order_count=(distribution_order_count+1) WHERE id = #{distributionId}
		""")
    void addCanRebate(BigDecimal commissionFrozen, String distributionId);
}
