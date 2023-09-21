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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册Controller
 *
 * @author shuigedeng
 * @since 2021-02-13
 */
@Api(tags = "注册")
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterApplicationService registerApplicationService;

    /** 注册租户 */
    @ApiOperation("注册租户")
    @SysLog("注册租户")
    @PostMapping("/tenant")
    public Result registerTenantAndUser(@RequestBody RegisterTenantCommand registerTenantCommand) {
        ValidatorUtils.validateEntity(registerTenantCommand, AddGroup.class);
        registerApplicationService.registerTenant(registerTenantCommand);
        return Result.ok();
    }
}
