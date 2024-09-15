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

package com.taotao.cloud.goods.facade.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,商品规格接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-商品规格管理API", description = "管理端-商品规格管理API")
@RequestMapping("/goods/manager/spec")
public class SpecificationManagerController {

    /**
     * 商品规格服务
     */
    private final ISpecificationService specificationService;

    @Operation(summary = "获取所有可用规格", description = "获取所有可用规格")
    @RequestLogger("获取所有可用规格")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/all")
    public Result<List<SpecificationCO>> getAll() {
        List<Specification> specifications = specificationService.list();
        return Result.success(SpecificationConvert.INSTANCE.convert(specifications));
    }

    @Operation(summary = "搜索规格", description = "搜索规格")
    @RequestLogger("搜索规格")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping
    public Result<PageResult<SpecificationCO>> page(@Validated SpecificationPageQuery specificationPageQuery) {
        IPage<Specification> specificationPage = specificationService.getPage(specificationPageQuery);
        return Result.success(MpUtils.convertMybatisPage(specificationPage, SpecificationConvert.INSTANCE::convert));
    }

    @Operation(summary = "保存规格", description = "保存规格")
    @RequestLogger("保存规格")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<Boolean> save(@Valid @RequestBody SpecificationDTO specificationDTO) {
        Specification specification = SpecificationConvert.INSTANCE.convert(specificationDTO);
        return Result.success(specificationService.save(specification));
    }

    @Operation(summary = "更改规格", description = "更改规格")
    @Parameters({
            @Parameter(name = "id", required = true, description = "id", in = ParameterIn.PATH),
    })
    @RequestLogger("更改规格")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{id}")
    public Result<Boolean> update(@Valid @RequestBody SpecificationDTO specificationDTO,
                                  @PathVariable Long id) {
        Specification specification = SpecificationConvert.INSTANCE.convert(specificationDTO);
        specification.setId(id);

        return Result.success(specificationService.saveOrUpdate(specification));
    }

    @Operation(summary = "批量删除", description = "批量删除")
    @Parameters({
            @Parameter(name = "ids", required = true, description = "id列表,逗号连接", example = "1,2,3"),
    })
    @RequestLogger("批量删除")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping("/batch")
    public Result<Boolean> delAllByIds(@Valid @NotNull(message = "id列表不能为空") @Size(min = 1, max = 3, message = "id个数只能在1至3个")
                                           @RequestParam List<Long> ids) {
        return Result.success(specificationService.deleteSpecification(ids));
    }
}
