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

package com.taotao.cloud.auth.biz.controller;

import com.taotao.boot.common.model.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.cloud.auth.biz.jpa.service.TtcAuthorizationConsentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MessagesController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-22 15:56:32
 */
@Validated
@Tag(name = "测试API", description = "测试API")
@RestController
@RequestMapping("/api/test")
public class MessagesController {

    @Autowired private JwtDecoder jwtDecoder;
    @Autowired private TtcAuthorizationConsentService ttcAuthorizationConsentService;

    //    @Operation(summary = "测试消息NotAuth", description = "测试消息NotAuth")
    //    @GetMapping("/NotAuth/myPageQuery")
    //    @NotAuth
    //    public Result<String> myPageQuery() {
    //        Page<TtcAuthorizationConsent> ttcAuthorizationConsents =
    // ttcAuthorizationConsentService.myPageQuery(null, null);
    //
    //        return Result.success("sadfasdf");
    //    }

    // @Autowired
    // private IFeignDictApi feignDictService;
    //
    // @DubboReference(check = false)
    // private IDubboDictService dubboDictService;

    @Operation(summary = "测试消息", description = "测试消息")
    @GetMapping("/messages")
    public String[] getMessages() {
        LogUtils.info("slfdlaskdf;lasjdf;lj");
        // DubboDictRes dubboDictRes = dubboDictService.findByCode(1);

        // try {
        //	FeignDictResponse feignDictResponse = feignDictService.findByCode("sd");
        // } catch (Exception e) {
        //	LogUtils.error(e);
        //	throw new RuntimeException(e);
        // }

        return new String[] {"Message 1", "Message 2", "Message 3"};
    }

    @Operation(summary = "测试消息NotAuth", description = "测试消息NotAuth")
    @GetMapping("/NotAuth/messages")
    @NotAuth
    public String[] getMessagesNotAuth() {
        LogUtils.info("slfdlaskdf;lasjdf;lj NotAuth");

        return new String[] {"Message 1", "Message 2", "Message 3"};
    }

    /**
     * 获取当前认证的OAuth2用户信息，默认是保存在{@link jakarta.servlet.http.HttpSession}中的
     *
     * @param user OAuth2用户信息
     * @return OAuth2用户信息
     */
    @Operation(summary = "获取当前认证的OAuth2用户信息", description = "获取当前认证的OAuth2用户信息")
    @GetMapping("/user")
    public Result<OAuth2User> user(@AuthenticationPrincipal OAuth2User user) {
        return Result.success(user);
    }

    //    @Operation(summary = "获取当前认证的OAuth2客户端信息", description = "v")
    //    @GetMapping("/client")
    //    public Result<OAuth2AuthorizedClient> client(
    //            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient) {
    //        return Result.success(oAuth2AuthorizedClient);
    //    }

    @GetMapping(value = "/info")
    public Object getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String tokenValue = jwtAuthenticationToken.getToken().getTokenValue();
        Jwt decode = jwtDecoder.decode(tokenValue);

        return authentication.getPrincipal();
    }
}
