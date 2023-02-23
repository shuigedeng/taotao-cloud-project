/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.xhuicloud.logs.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhuicloud.common.authorization.resource.annotation.Anonymous;
import com.xhuicloud.common.core.utils.Response;
import com.xhuicloud.logs.entity.SysLog;
import com.xhuicloud.logs.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @program: XHuiCloud
 * @description: SysLogController
 * @author: Sinda
 * @create: 2020-02-01 00:32
 */
@RestController
@RequestMapping("/log")
@AllArgsConstructor
@Api(value = "log", tags = "日志管理模块")
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param sysLog 系统日志
     * @return Response
     */
    @GetMapping("/page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public Response<Page> page(Page page, SysLog sysLog) {
        return Response.success(sysLogService.page(page, Wrappers.query(sysLog).orderByDesc("id")));
    }

    @Anonymous
    @PostMapping("/save")
    public Response save(@RequestBody SysLog sysLog) {
        return Response.success(sysLogService.save(sysLog));
    }

}
