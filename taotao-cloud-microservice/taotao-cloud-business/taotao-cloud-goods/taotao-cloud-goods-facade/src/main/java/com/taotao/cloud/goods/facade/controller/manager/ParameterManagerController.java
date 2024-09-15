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
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,分类绑定参数组管理接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-参数管理API", description = "管理端-参数管理API")
@RequestMapping("/goods/manager/parameters")
public class ParameterManagerController {

    /** 参数服务 */
    private final IParametersService parametersService;

    @Operation(summary = "添加参数", description = "添加参数")
    @RequestLogger("添加参数添加参数")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<Boolean> save(@Validated @RequestBody ParametersDTO parametersDTO) {
        Parameters parameters = ParametersConvert.INSTANCE.convert(parametersDTO);
        return Result.success(parametersService.save(parameters));
    }

    @Operation(summary = "编辑参数", description = "编辑参数")
    @io.swagger.v3.oas.annotations.Parameters({
            @Parameter(name = "id", required = true, description = "id", in = ParameterIn.PATH),
    })
    @RequestLogger("编辑参数")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{id}")
    public Result<Boolean> update(@Validated @RequestBody ParametersDTO parametersDTO,
                                  @PathVariable Long id) {
        Parameters parameters = ParametersConvert.INSTANCE.convert(parametersDTO);
        parameters.setId(id);
        return Result.success(parametersService.updateParameter(parameters));
    }

    @Operation(summary = "根据id删除参数", description = "根据id删除参数")
    @io.swagger.v3.oas.annotations.Parameters({
            @Parameter(name = "id", required = true, description = "id", in = ParameterIn.PATH),
    })
    @RequestLogger("根据id删除参数")
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delById(@PathVariable Long id) {
        return Result.success(parametersService.removeById(id));
    }
}
