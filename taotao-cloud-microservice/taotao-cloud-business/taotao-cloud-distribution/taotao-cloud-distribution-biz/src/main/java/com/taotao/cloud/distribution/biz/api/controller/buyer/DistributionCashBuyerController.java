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
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.distribution.biz.model.entity.DistributionCash;
import com.taotao.cloud.distribution.biz.service.IDistributionCashService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/** 买家端,分销商品佣金提现接口 */
@Validated
@RestController
@Tag(name = "买家端-分销商品佣金提现接口", description = "买家端-分销商品佣金提现接口")
@RequestMapping("/buyer/distribution/cash")
public class DistributionCashBuyerController {

    /** 分销佣金 */
    @Autowired
    private IDistributionCashService distributionCashService;
    /** 分销员提现 */
    @Autowired
    private IDistributionCashService distributorCashService;

    @Operation(summary = "分销员提现", description = "分销员提现")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    // @PreventDuplicateSubmissions
    @PostMapping
    public Result<Object> cash(
            @Validated
                    @Max(value = 9999, message = "提现金额单次最多允许提现9999元")
                    @Min(value = 1, message = "提现金额单次最少提现金额为1元")
                    @NotNull
                    @ApiIgnore
                    BigDecimal price) {
        if (Boolean.TRUE.equals(distributionCashService.cash(price))) {
            return Result.success();
        }
        throw new BusinessException(ResultEnum.ERROR);
    }

    @Operation(summary = "分销员提现历史", description = "分销员提现历史")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping
    public Result<IPage<DistributionCash>> casHistory(PageVO page) {
        return Result.success(distributorCashService.getDistributionCash(page));
    }
}
