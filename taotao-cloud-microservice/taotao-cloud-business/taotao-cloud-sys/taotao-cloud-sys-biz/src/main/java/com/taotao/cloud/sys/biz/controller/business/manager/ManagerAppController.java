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

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.data.mybatis.mybatisplus.MpUtils;
import com.taotao.cloud.sys.biz.model.dto.app.AppDTO;
import com.taotao.cloud.sys.biz.model.dto.app.AppPageDTO;
import com.taotao.cloud.sys.biz.service.business.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统应用表
 *
 * @author shuigedeng
 * @version 2023.05
 * @since 2023-05-05 17:12:15
 */
@Tag(name = "应用管理")
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class ManagerAppController {

    private final AppService appService;

    @Operation(summary = "分页")
    @GetMapping(value = "/page")
    public Result<PageResult<AppDTO>> pageSysApp(AppPageDTO appPageDTO) {
        // Validator.validateParam(appPageDTO, ValidationGroups.query.class);
        return Result.success(MpUtils.convertMybatisPage(appService.pageApp(appPageDTO), AppDTO.class));
    }

    @Operation(summary = "添加")
    @PostMapping(value = "/add")
    public Result<Boolean> add(@RequestBody AppDTO appDTO) {
        // ValidationUtil.validateParam(appDTO, ValidationGroups.add.class);
        return Result.success(appService.addApp(appDTO));
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public Result<Boolean> update(@RequestBody AppDTO appDTO) {
        // ValidationUtil.validateParam(appDTO, ValidationGroups.update.class);
        return Result.success(appService.updateApp(appDTO));
    }

    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete")
    public Result<Boolean> delete(Long id) {
        return Result.success(appService.deleteApp(id));
    }

    @Operation(summary = "获取单条")
    @GetMapping(value = "/findById")
    public Result<AppDTO> findById(Long id) {
        return Result.success(appService.findById(id));
    }

    @Operation(summary = "获取全部")
    @GetMapping(value = "/findAll")
    public Result<List<AppDTO>> findAll() {
        return Result.success(appService.findAll());
    }
}
