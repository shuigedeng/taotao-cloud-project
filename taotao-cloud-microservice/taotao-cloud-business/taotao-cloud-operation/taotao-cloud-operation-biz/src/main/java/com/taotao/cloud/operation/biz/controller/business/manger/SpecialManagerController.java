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

package com.taotao.cloud.operation.biz.controller.business.manger;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.operation.biz.model.entity.Special;
import com.taotao.cloud.operation.biz.service.business.SpecialService;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 管理端,专题活动接口 */
@RestController
@Tag(name = "管理端,专题活动接口")
@RequestMapping("/manager/order/special")
public class SpecialManagerController {

    @Autowired
    private SpecialService specialService;

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "添加专题活动")
    @PostMapping("/addSpecial")
    public Result<Special> addSpecial(@Valid Special special) {
        return Result.success(specialService.addSpecial(special));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "修改专题活动")
    @PutMapping("/updateSpecial")
    public Result<Special> updateSpecial(
            @Parameter(description = "专题ID") @PathVariable Long id, @Valid Special special) {
        special.setId(id);
        specialService.updateById(special);
        return Result.success(special);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "删除专题活动")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSpecial(@Parameter(description = "专题ID") @PathVariable String id) {
        specialService.removeSpecial(id);
        return Result.success(true);
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "分页获取专题活动")
    @GetMapping
    public Result<IPage<Special>> getSpecials(PageVO pageVo) {
        return Result.success(specialService.page(PageUtil.initPage(pageVo)));
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取专题活动列表")
    @GetMapping("/getSpecialsList")
    public Result<List<Special>> getSpecialsList() {
        return Result.success(specialService.list());
    }

    @RequestLogger
    @PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @Operation(summary = "获取专题活动")
    @GetMapping(value = "/{id}")
    public Result<Special> getSpecialsList(@Parameter(description = "专题ID", required = true) @PathVariable String id) {
        return Result.success(specialService.getById(id));
    }
}
