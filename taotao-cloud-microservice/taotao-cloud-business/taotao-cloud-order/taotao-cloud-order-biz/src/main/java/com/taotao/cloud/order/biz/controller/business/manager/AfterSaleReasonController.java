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

package com.taotao.cloud.order.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.order.sys.model.dto.aftersale.AfterSaleReasonDTO;
import com.taotao.cloud.order.sys.model.page.aftersale.AfterSaleReasonPageQuery;
import com.taotao.cloud.order.sys.model.vo.aftersale.AfterSaleReasonVO;
import com.taotao.cloud.order.biz.model.convert.AfterSaleReasonConvert;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleReason;
import com.taotao.cloud.order.biz.service.business.aftersale.IAfterSaleReasonService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,售后原因API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:11
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-售后原因管理API", description = "管理端-售后原因管理API")
@RequestMapping("/order/manager/aftersale/reason")
public class AfterSaleReasonController {

    /** 售后原因 */
    private final IAfterSaleReasonService afterSaleReasonService;

    @Operation(summary = "查看售后原因", description = "查看售后原因")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{id}")
    public Result<AfterSaleReasonVO> getById(@PathVariable String id) {
        AfterSaleReason afterSaleReason = afterSaleReasonService.getById(id);
        return Result.success(AfterSaleReasonConvert.INSTANCE.convert(afterSaleReason));
    }

    @Operation(summary = "分页获取售后原因", description = "分页获取售后原因")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/page")
    public Result<PageResult<AfterSaleReasonVO>> getByPage(
            @Validated AfterSaleReasonPageQuery afterSaleReasonPageQuery) {
        IPage<AfterSaleReason> page = afterSaleReasonService.pageQuery(afterSaleReasonPageQuery);
        return Result.success(MpUtils.convertMybatisPage(page, AfterSaleReasonVO.class));
    }

    @Operation(summary = "添加售后原因", description = "添加售后原因")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<Boolean> save(@Validated @RequestBody AfterSaleReasonDTO afterSaleReasonDTO) {
        return Result.success(afterSaleReasonService.save(AfterSaleReasonConvert.INSTANCE.convert(afterSaleReasonDTO)));
    }

    @Operation(summary = "修改售后原因", description = "修改售后原因")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{id}")
    public Result<Boolean> update(
            @Validated @RequestBody AfterSaleReasonDTO afterSaleReasonDTO, @PathVariable("id") Long id) {
        AfterSaleReason afterSaleReason = AfterSaleReasonConvert.INSTANCE.convert(afterSaleReasonDTO);
        afterSaleReason.setId(id);
        return Result.success(afterSaleReasonService.editAfterSaleReason(afterSaleReason));
    }

    @Operation(summary = "删除售后原因", description = "删除售后原因")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delAllByIds(@PathVariable String id) {
        return Result.success(afterSaleReasonService.removeById(id));
    }
}
