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

import com.taotao.boot.common.model.Result;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,商品分类规格接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-商品分类规格管理API", description = "管理端-商品分类规格管理API")
@RequestMapping("/goods/manager/category/spec")
public class CategorySpecificationManagerController {

    /** 分类规格服务 */
    private final ICategorySpecificationService categorySpecificationService;
    /** 规格服务 */
    private final ISpecificationService specificationService;

    @Operation(summary = "查询某分类下绑定的规格信息", description = "查询某分类下绑定的规格信息")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{categoryId}")
    public Result<List<Specification>> getCategorySpec(@PathVariable Long categoryId) {
        return Result.success(categorySpecificationService.getCategorySpecList(categoryId));
    }

    @Operation(summary = "查询某分类下绑定的规格信息,商品操作使用", description = "查询某分类下绑定的规格信息,商品操作使用")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/goods/{categoryId}")
    public Result<List<Specification>> getSpec(@PathVariable Long categoryId) {
        return Result.success(specificationService.list());
    }

    @Operation(summary = "保存某分类下绑定的规格信息", description = "保存某分类下绑定的规格信息")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/{categoryId}")
    public Result<Boolean> saveCategoryBrand(@PathVariable Long categoryId, @RequestParam String[] categorySpecs) {
        return Result.success(specificationService.saveCategoryBrand(categoryId, categorySpecs));
    }
}
