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

package com.taotao.cloud.sa.just.biz.just.justauth.feign;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocial;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocialUser;
import com.taotao.cloud.sa.just.biz.just.justauth.service.IJustAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * JustAuthFeign
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@RestController
@RequestMapping(value = "/feign/justauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "JustAuthFeign|提供微服务调用接口")
@RefreshScope
public class JustAuthFeign {

    private final IJustAuthService justAuthService;

    @GetMapping(value = "/user/bind/id")
    @ApiOperation(value = "查询第三方用户绑定关系", notes = "查询第三方用户绑定关系")
    Result<Object> userBindId(
            @NotBlank @RequestParam("uuid") String uuid, @NotBlank @RequestParam("source") String source ) {
        Long userId = justAuthService.userBindId(uuid, source);
        return Result.success(userId);
    }

    @PostMapping(value = "/user/create/or/update")
    @ApiOperation(value = "创建或更新第三方用户信息", notes = "创建或更新第三方用户信息")
    Result<Object> userCreateOrUpdate( @NotNull @RequestBody JustAuthSocialInfoDTO justAuthSocialInfoDTO ) {
        Long socialId = justAuthService.userCreateOrUpdate(justAuthSocialInfoDTO);
        return Result.success(socialId);
    }

    @GetMapping(value = "/user/bind/query")
    @ApiOperation(value = "查询绑定第三方用户信息", notes = "查询绑定第三方用户信息")
    Result<Object> userBindQuery( @NotNull @RequestParam("socialId") Long socialId ) {
        return justAuthService.userBindQuery(socialId);
    }

    /**
     * 查询第三方用户信息
     */
    @GetMapping(value = "/social/info/query")
    Result<Object> querySocialInfo( @NotNull @RequestParam("socialId") Long socialId ) {
        JustAuthSocial justAuthSocial = justAuthService.querySocialInfo(socialId);
        return Result.success(justAuthSocial);
    }

    @GetMapping(value = "/user/bind")
    @ApiOperation(value = "绑定第三方用户信息", notes = "绑定第三方用户信息")
    Result<JustAuthSocialUser> userBind(
            @NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId ) {
        JustAuthSocialUser justAuthSocialUser = justAuthService.userBind(socialId, userId);
        return Result.success(justAuthSocialUser);
    }

    @GetMapping(value = "/user/unbind")
    @ApiOperation(value = "解绑第三方用户信息", notes = "解绑第三方用户信息")
    Result<JustAuthSocialUser> userUnbind(
            @NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId ) {
        return justAuthService.userUnbind(socialId, userId);
    }
}
