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

package com.taotao.cloud.distribution.biz.api.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.distribution.api.model.dto.DistributionApplyDTO;
import com.taotao.cloud.distribution.api.model.query.DistributionOrderPageQuery;
import com.taotao.cloud.distribution.biz.model.entity.Distribution;
import com.taotao.cloud.distribution.biz.model.entity.DistributionOrder;
import com.taotao.cloud.distribution.biz.service.IDistributionOrderService;
import com.taotao.cloud.distribution.biz.service.IDistributionService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 买家端,分销员接口 */
@Validated
@RestController
@Tag(name = "买家端-分销员接口", description = "买家端-分销员接口")
@RequestMapping("/buyer/distribution/distribution")
public class DistributionBuyerController {

    /** 分销员 */
    @Autowired
    private IDistributionService distributionService;
    /** 分销员订单 */
    @Autowired
    private IDistributionOrderService distributionOrderService;

    @Operation(summary = "申请分销员", description = "申请分销员")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<Object> applyDistribution(DistributionApplyDTO distributionApplyDTO) {
        return Result.success(distributionService.applyDistribution(distributionApplyDTO));
    }

    @Operation(summary = "获取分销员分页订单列表", description = "获取分销员分页订单列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/distributionOrder")
    public Result<IPage<DistributionOrder>> distributionOrderPage(
            DistributionOrderPageQuery distributionOrderPageQuery) {
        distributionOrderPageQuery.setDistributionId(
                distributionService.getDistribution().getId());
        return Result.success(distributionOrderService.getDistributionOrderPage(distributionOrderPageQuery));
    }

    @Operation(summary = "获取当前会员的分销员信息,可根据分销员信息查询待提现金额以及冻结金额等信息", description = "获取当前会员的分销员信息,可根据分销员信息查询待提现金额以及冻结金额等信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping
    public Result<Distribution> getDistribution() {
        // 检查分销开关
        distributionService.checkDistributionSetting();
        return Result.success(distributionService.getDistribution());
    }

    @Operation(summary = "绑定分销员", description = "绑定分销员")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/bindingDistribution/{distributionId}")
    public Result<Boolean> bindingDistribution(@PathVariable String distributionId) {
        distributionService.bindingDistribution(distributionId);
        return Result.success(true);
    }
}
