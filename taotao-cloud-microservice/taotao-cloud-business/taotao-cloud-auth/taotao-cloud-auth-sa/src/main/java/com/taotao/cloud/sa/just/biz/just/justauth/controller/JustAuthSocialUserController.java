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

package com.taotao.cloud.sa.just.biz.just.justauth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.service.IJustAuthSocialUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方用户绑定 前端控制器
 *
 * @since 2022-05-19
 */
@RestController
@RequestMapping("/extension/justauth/social/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(
        value = "JustAuthSocialUserController|第三方用户绑定前端控制器",
        tags = {"第三方用户绑定"})
@RefreshScope
public class JustAuthSocialUserController {

    private final IJustAuthSocialUserService justAuthSocialUserService;

    /**
     * 查询第三方用户绑定列表
     *
     * @param queryJustAuthSocialUserDTO
     * @param page
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询第三方用户绑定列表")
    public Result<Page<JustAuthSocialUserDTO>> list(
            QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO, Page<JustAuthSocialUserDTO> page) {
        Page<JustAuthSocialUserDTO> pageJustAuthSocialUser =
                justAuthSocialUserService.queryJustAuthSocialUserList(page, queryJustAuthSocialUserDTO);
        return Result.success(pageJustAuthSocialUser);
    }

    /**
     * 查询第三方用户绑定详情
     *
     * @param queryJustAuthSocialUserDTO
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "查询第三方用户绑定详情")
    public Result<?> query(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO) {
        JustAuthSocialUserDTO justAuthSocialUserDTO =
                justAuthSocialUserService.queryJustAuthSocialUser(queryJustAuthSocialUserDTO);
        return Result.success(justAuthSocialUserDTO);
    }

    /**
     * 添加第三方用户绑定
     *
     * @param justAuthSocialUser
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加第三方用户绑定")
    public Result<?> create(@RequestBody CreateJustAuthSocialUserDTO justAuthSocialUser) {
        justAuthSocialUserService.createJustAuthSocialUser(justAuthSocialUser);
        return Result.success();
    }

    /**
     * 修改第三方用户绑定
     *
     * @param justAuthSocialUser
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新第三方用户绑定")
    public Result<?> update(@RequestBody UpdateJustAuthSocialUserDTO justAuthSocialUser) {
        boolean result = justAuthSocialUserService.updateJustAuthSocialUser(justAuthSocialUser);
        return Result.success(result);
    }

    /**
     * 删除第三方用户绑定
     *
     * @param justAuthSocialUserId
     * @return
     */
    @PostMapping("/delete/{justAuthSocialUserId}")
    @ApiOperation(value = "删除第三方用户绑定")
    @ApiImplicitParam(
            paramType = "path",
            name = "justAuthSocialUserId",
            value = "第三方用户绑定ID",
            required = true,
            dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("justAuthSocialUserId") Long justAuthSocialUserId) {
        if (null == justAuthSocialUserId) {
            return Result.error("ID不能为空");
        }
        boolean result = justAuthSocialUserService.deleteJustAuthSocialUser(justAuthSocialUserId);
        return Result.success(result);
    }

    /**
     * 批量删除第三方用户绑定
     *
     * @param justAuthSocialUserIds
     * @return
     */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除第三方用户绑定")
    @ApiImplicitParam(
            name = "justAuthSocialUserIds",
            value = "第三方用户绑定ID列表",
            required = true,
            dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> justAuthSocialUserIds) {
        if (CollectionUtils.isEmpty(justAuthSocialUserIds)) {
            return Result.error("第三方用户绑定ID列表不能为空");
        }
        boolean result = justAuthSocialUserService.batchDeleteJustAuthSocialUser(justAuthSocialUserIds);
        return Result.success(result);
    }
}
