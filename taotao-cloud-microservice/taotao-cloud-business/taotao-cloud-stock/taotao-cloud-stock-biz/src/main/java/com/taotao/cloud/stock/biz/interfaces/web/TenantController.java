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

package com.taotao.cloud.stock.biz.interfaces.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 租户Controller
 *
 * @author shuigedeng
 * @since 2021-02-24
 */
@Api(tags = "租户管理")
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantQueryService tenantQueryService;

    @Autowired
    private TenantApplicationService tenantApplicationService;

    /** 用户分页查询 */
    @ApiOperation("租户分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:tenant:list')")
    public Result list(@RequestParam Map<String, Object> params) {
        Page page = tenantQueryService.queryPage(params);
        return Result.ok().put(PageConstant.PAGE, page);
    }

    /** 禁用租户 */
    @ApiOperation("禁用租户")
    @SysLog("禁用租户")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:tenant:disable')")
    public Result disable(@PathVariable("id") String id) {
        tenantApplicationService.disable(id);
        return Result.ok();
    }
}
