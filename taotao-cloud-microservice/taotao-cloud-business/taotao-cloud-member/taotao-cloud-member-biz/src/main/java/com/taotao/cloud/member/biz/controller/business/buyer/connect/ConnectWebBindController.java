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

package com.taotao.cloud.member.biz.controller.business.buyer.connect;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.member.biz.connect.entity.dto.AuthCallback;
import com.taotao.cloud.member.biz.connect.entity.dto.ConnectAuthUser;
import com.taotao.cloud.member.biz.connect.request.AuthRequest;
import com.taotao.cloud.member.biz.connect.service.ConnectService;
import com.taotao.cloud.member.biz.connect.token.Token;
import com.taotao.cloud.member.biz.connect.util.ConnectUtil;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 买家端,web联合登录 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-web联合登录API", description = "买家端-web联合登录API")
@RequestMapping("/member/buyer/passport/connect/connect")
public class ConnectWebBindController {

    private final ConnectService connectService;
    private final IMemberService memberService;
    private final ConnectUtil connectUtil;

    @Operation(summary = "WEB信任登录授权", description = "WEB信任登录授权")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping("/login/web/{type}")
    public Result<String> webAuthorize(
            @Parameter(name = "type", description = "登录方式:QQ,微信,微信_PC QQ,WECHAT,WECHAT_PC") @PathVariable String type,
            HttpServletResponse response)
            throws IOException {
        AuthRequest authRequest = connectUtil.getAuthRequest(type);
        String authorizeUrl = authRequest.authorize(UUID.fastUUID().toString());
        response.sendRedirect(authorizeUrl);
        return Result.success(authorizeUrl);
    }

    @Operation(summary = "信任登录统一回调地址", description = "信任登录统一回调地址")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping("/callback/{type}")
    public void callBack(
            @PathVariable String type,
            AuthCallback callback,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
            throws IOException {
        connectUtil.callback(type, callback, httpServletRequest, httpServletResponse);
    }

    @Operation(summary = "信任登录响应结果获取", description = "信任登录响应结果获取")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping("/result")
    public Result<Object> callBackResult(String state) {
        if (state == null) {
            throw new BusinessException(ResultEnum.USER_CONNECT_LOGIN_ERROR);
        }
        return connectUtil.getResult(state);
    }

    @Operation(summary = "WEB信任登录授权", description = "WEB信任登录授权")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping("/register/auto")
    public Result<Token> webAuthorize() {
        Token token = memberService.autoRegister();
        return Result.success(token);
    }

    @Operation(summary = "unionID登录", description = "unionID登录")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    // @ApiImplicitParams({
    //	@ApiImplicitParam(name = "openId", value = "openid", required = true, paramType = "query"),
    //	@ApiImplicitParam(name = "type", value = "联合类型", required = true,
    //		allowableValues = "WECHAT,QQ,ALIPAY,WEIBO,APPLE", paramType = "query"),
    //	@ApiImplicitParam(name = "uniAccessToken", value = "联合登陆返回的accessToken", required = true,
    // paramType = "query")
    // })
    @GetMapping("/app/login")
    public Result<Token> unionLogin(ConnectAuthUser authUser, @RequestHeader("uuid") String uuid) {
        try {
            return Result.success(connectService.appLoginCallback(authUser, uuid));
        } catch (Exception e) {
            LogUtils.error("unionID登录错误", e);
        }
        return null;
    }
}
