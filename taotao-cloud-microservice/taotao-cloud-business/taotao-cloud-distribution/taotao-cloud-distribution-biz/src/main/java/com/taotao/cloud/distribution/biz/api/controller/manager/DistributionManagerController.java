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

package com.taotao.cloud.distribution.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.distribution.api.model.query.DistributionPageQuery;
import com.taotao.cloud.distribution.biz.model.entity.Distribution;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 管理端,分销员管理接口 */
@Validated
@RestController
@Tag(name = "管理端,分销员管理接口", description = "管理端,分销员管理接口")
@RequestMapping("/manager/distribution/distribution")
public class DistributionManagerController {

    @Autowired
    private DistributionService distributionService;

    @Operation(summary = "分页获取", description = "分页获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/getByPage")
    public Result<IPage<Distribution>> getByPage(DistributionPageQuery distributionPageQuery, PageVO page) {
        return Result.success(distributionService.distributionPage(distributionPageQuery, page));
    }

    @Operation(summary = "清退分销商", description = "清退分销商")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/tree")
    @PreventDuplicateSubmissions
    @PutMapping(value = "/retreat/{id}")
    public Result<Object> retreat(@Parameter(description = "分销商id") @PathVariable String id) {
        if (distributionService.retreat(id)) {
            return Result.success();
        } else {
            throw new BusinessException(ResultEnum.DISTRIBUTION_RETREAT_ERROR);
        }
    }

    @Operation(summary = "恢复分销商", description = "恢复分销商")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/tree")
    @PreventDuplicateSubmissions
    @PutMapping(value = "/resume/{id}")
    public Result<Object> resume(@PathVariable String id) {
        if (distributionService.resume(id)) {
            return Result.success();
        } else {
            throw new BusinessException(ResultEnum.DISTRIBUTION_RETREAT_ERROR);
        }
    }

    @Operation(summary = "审核分销商", description = "审核分销商")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PreventDuplicateSubmissions
    @PutMapping(value = "/audit/{id}")
    public Result<Object> audit(
            @NotNull @PathVariable String id,
            @Parameter(description = "审核结果，PASS 通过  REFUSE 拒绝") @NotNull String status) {
        if (distributionService.audit(id, status)) {
            return Result.success();
        } else {
            throw new BusinessException(ResultEnum.DISTRIBUTION_AUDIT_ERROR);
        }
    }
}
