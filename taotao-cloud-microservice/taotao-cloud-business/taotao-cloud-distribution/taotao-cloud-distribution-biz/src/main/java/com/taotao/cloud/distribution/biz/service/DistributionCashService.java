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

package com.taotao.cloud.distribution.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.distribution.api.model.vo.DistributionCashSearchVO;
import com.taotao.cloud.distribution.biz.model.entity.DistributionCash;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/** 分销佣金业务层 */
public interface DistributionCashService extends IService<DistributionCash> {

    /**
     * 提交分销提现申请
     *
     * @param applyMoney 申请金额
     * @return 操作状态
     */
    Boolean cash(BigDecimal applyMoney);

    /**
     * 获取当前会员的分销提现分页列表
     *
     * @param page 分页
     * @return 申请提现分页
     */
    IPage<DistributionCash> getDistributionCash(PageVO page);

    /**
     * 获取分销员提现分页列表
     *
     * @param distributionCashSearchVO 搜索条件
     * @return 分销员提现分页列表
     */
    IPage<DistributionCash> getDistributionCash(DistributionCashSearchVO distributionCashSearchVO);

    /**
     * 审核分销提现申请
     *
     * @param id 分销提现申请ID
     * @param result 处理结果
     * @return 分销提现申请
     */
    DistributionCash audit(@PathVariable String id, @RequestParam String result);
}
