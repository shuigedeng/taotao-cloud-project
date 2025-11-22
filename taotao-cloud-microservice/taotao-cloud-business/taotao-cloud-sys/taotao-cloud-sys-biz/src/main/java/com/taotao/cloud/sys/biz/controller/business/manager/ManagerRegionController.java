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

package com.taotao.cloud.sys.biz.controller.business.manager;

import com.taotao.boot.common.model.request.BaseQuery;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.tree.ForestNodeMerger;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.cloud.sys.biz.model.dto.region.RegionSaveDTO;
import com.taotao.cloud.sys.biz.model.dto.region.RegionUpdateDTO;
import com.taotao.cloud.sys.biz.model.vo.region.RegionParentVO;
import com.taotao.cloud.sys.biz.model.vo.region.RegionQueryVO;
import com.taotao.cloud.sys.biz.model.vo.region.RegionTreeVO;
import com.taotao.cloud.sys.biz.model.entity.region.Region;
import com.taotao.cloud.sys.biz.service.business.IRegionService;
import com.taotao.boot.webagg.controller.BaseSuperController;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-地区管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 15:01:59
 */
@RestController
@RequestMapping("/sys/manager/region")
@Tag(name = "管理端-地区管理API", description = "管理端-地区管理API")
public class ManagerRegionController
        extends BaseSuperController<
        IRegionService, Region, Long, BaseQuery, RegionSaveDTO, RegionUpdateDTO, RegionQueryVO> {

    @Operation(summary = "根据父id查询地区数据", description = "根据父id查询地区数据")
    @Parameters({
            @Parameter(name = "parentId", description = "父id", required = true, example = "1111", in = ParameterIn.PATH)
    })
    @RequestLogger
    @GetMapping("/parentId/{parentId}")
    @NotAuth
    // @PreAuthorize("hasAuthority('sys:region:info:parentId')")
    public Result<List<RegionParentVO>> queryRegionByParentId(@NotBlank(message = "父id不能为空") @PathVariable(name = "parentId") Long parentId) {
        List<RegionParentVO> result = service().queryRegionByParentId(parentId);
        return Result.success(result);
    }

    @Operation(summary = "树形结构查询", description = "树形结构查询")
    @Parameters({
            @Parameter(name = "parentId", description = "父id", required = true, example = "1"),
            @Parameter(name = "depth", description = "深度", example = "1024"),
    })
    @RequestLogger
    @GetMapping(value = "/tree")
    @NotAuth
    // @PreAuthorize("hasAuthority('sys:region:info:true')")
    public Result<List<RegionParentVO>> tree(@NotNull(message = "父id不能为空") @RequestParam(required = false, defaultValue = "1") Long parentId,
                                             @RequestParam(required = false, defaultValue = "1024") Integer depth) {
        List<RegionParentVO> result = service().tree(parentId, depth);
        return Result.success(result);
    }

    @Operation(summary = "另一种树形结构查询", description = "另一种树形结构查询")
    @RequestLogger
    @GetMapping(value = "/other/tree")
    @PreAuthorize("hasAuthority('sys:region:info:true')")
    public Result<List<RegionTreeVO>> treeOther() {
        List<RegionTreeVO> result = service().treeOther();
        return Result.success(ForestNodeMerger.merge(result));
    }
}
