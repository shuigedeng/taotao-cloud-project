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
import com.taotao.boot.common.model.PageQuery;
import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.common.model.Result;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端,商品计量单位接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-商品计量单位管理API", description = "管理端-商品计量单位管理API")
@RequestMapping("/goods/manager/goods/unit")
public class GoodsUnitManagerController {

    /** 商品计量服务 */
    private final IGoodsUnitService goodsUnitService;

    @Operation(summary = "分页获取商品计量单位", description = "分页获取商品计量单位")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("分页获取商品计量单位")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/page")
    public Result<PageResult<GoodsUnit>> getByPage(PageQuery pageQuery) {
        IPage<GoodsUnit> page = goodsUnitService.page(pageQuery.buildMpPage());
        return Result.success(MpUtils.convertMybatisPage(page, GoodsUnit.class));
    }

    @Operation(summary = "获取商品计量单位", description = "获取商品计量单位")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("获取商品计量单位")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/{id}")
    public Result<GoodsUnit> getById(@NotNull @PathVariable Long id) {
        return Result.success(goodsUnitService.getById(id));
    }

    @Operation(summary = "添加商品计量单位", description = "添加商品计量单位")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("添加商品计量单位")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<Boolean> save(@Valid @RequestBody GoodsUnit goodsUnit) {
        return Result.success(goodsUnitService.save(goodsUnit));
    }

    @Operation(summary = "编辑商品计量单位", description = "编辑商品计量单位")
    @Parameters({
            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
    })
    @RequestLogger("编辑商品计量单位")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{id}")
    public Result<Boolean> update(@NotNull @PathVariable Long id, @Valid @RequestBody GoodsUnit goodsUnit) {
        goodsUnit.setId(id);
        return Result.success(goodsUnitService.updateById(goodsUnit));
    }

    @Operation(summary = "删除商品计量单位", description = "删除商品计量单位")
    @Parameters({
            @Parameter(name = "ids", required = true, description = "id列表,逗号连接", example = "1,2,3"),
    })
    @RequestLogger("删除商品计量单位")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@NotEmpty(message = "id不能为空") @RequestParam List<Long> ids) {
        return Result.success(goodsUnitService.removeByIds(ids));
    }
}
