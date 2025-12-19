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

package com.taotao.cloud.sys.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.sys.biz.model.entity.SysLog;
import com.taotao.cloud.sys.biz.service.SysLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SysLogController
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@RestController
@RequestMapping("/sys/log")
@AllArgsConstructor
@Tag(name = "工具管理端-日志管理模块", description = "工具管理端-日志管理模块")
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param sysLog 系统日志
     * @return Response
     */
    @GetMapping("/page")
    // @ApiOperation(value = "分页查询", notes = "分页查询")
    public Result<Page> page( Page page, SysLog sysLog ) {
//        return Result.success(sysLogService.page(page, Wrappers.query(sysLog).orderByDesc("id")));
        return null;
    }

    // @Anonymous
    @PostMapping("/save")
    public Result save( @RequestBody SysLog sysLog ) {
//        return Result.success(sysLogService.save(sysLog));
        return null;
    }
}
