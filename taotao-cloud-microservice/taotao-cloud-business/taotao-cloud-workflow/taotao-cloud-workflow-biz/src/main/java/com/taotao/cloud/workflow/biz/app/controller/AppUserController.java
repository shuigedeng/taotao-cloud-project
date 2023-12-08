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

package com.taotao.cloud.workflow.biz.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import taotao.cloud.workflow.biz.base.ActionResult;
import taotao.cloud.workflow.biz.model.AppUserInfoVO;
import taotao.cloud.workflow.biz.model.AppUsersVO;
import taotao.cloud.workflow.biz.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息
 *
 * @author 
 * 
 * 
 * @since 2021-07-08
 */
@Api(tags = "app用户信息", value = "User")
@RestController
@RequestMapping("/app/User")
public class AppUserController {

    @Autowired
    private AppService appService;

    /**
     * 用户信息
     *
     * @return
     */
    @ApiOperation("用户信息")
    @GetMapping
    public ActionResult getInfo() {
        AppUsersVO userAllVO = appService.userInfo();
        return ActionResult.success(userAllVO);
    }

    /**
     * 通讯录详情
     *
     * @return
     */
    @ApiOperation("通讯录详情")
    @GetMapping("/{id}")
    public ActionResult userInfo(@PathVariable("id") String id) {
        AppUserInfoVO userInfoVO = appService.getInfo(id);
        return ActionResult.success(userInfoVO);
    }
}
