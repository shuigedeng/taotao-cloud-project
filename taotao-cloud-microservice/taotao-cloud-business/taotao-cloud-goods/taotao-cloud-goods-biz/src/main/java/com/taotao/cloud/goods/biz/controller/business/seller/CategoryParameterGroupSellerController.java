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

package com.taotao.cloud.goods.biz.controller.business.seller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.goods.biz.model.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.service.business.ICategoryParameterGroupService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,分类绑定参数组接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-分类绑定参数组API", description = "店铺端-分类绑定参数组API")
@RequestMapping("/goods/seller/category/parameters")
public class CategoryParameterGroupSellerController {

    /** 分类参数组服务 */
    private final ICategoryParameterGroupService categoryParameterGroupService;

    @Operation(summary = "查询某分类下绑定的参数信息", description = "查询某分类下绑定的参数信息")
    @RequestLogger("查询某分类下绑定的参数信息")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{categoryId}")
    public Result<List<ParameterGroupVO>> getCategoryParam(@PathVariable("categoryId") Long categoryId) {
        return Result.success(categoryParameterGroupService.getCategoryParams(categoryId));
    }
}
