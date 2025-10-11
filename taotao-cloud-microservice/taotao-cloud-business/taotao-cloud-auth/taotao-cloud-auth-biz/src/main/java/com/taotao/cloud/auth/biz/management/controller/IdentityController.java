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

package com.taotao.cloud.auth.biz.management.controller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.auth.api.model.dto.SignInErrorPrompt;
import com.taotao.cloud.auth.api.model.dto.SignInErrorStatus;
import com.taotao.cloud.auth.biz.authentication.stamp.SignInFailureLimitedStampManager;
import com.taotao.cloud.auth.biz.management.dto.Session;
import com.taotao.cloud.auth.biz.management.dto.SessionCreate;
import com.taotao.cloud.auth.biz.management.dto.SessionExchange;
import com.taotao.cloud.auth.biz.management.entity.SecretKey;
import com.taotao.cloud.auth.biz.management.service.InterfaceSecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @see <a href="https://conkeyn.iteye.com/blog/2296406">参考文档</a>
 */
@RestController
@Tags({
    @Tag(name = "OAuth2 认证服务器接口"),
    @Tag(name = "OAuth2 认证服务器开放接口"),
    @Tag(name = "OAuth2 身份认证辅助接口")
})
public class IdentityController {

    private final Logger log = LoggerFactory.getLogger(IdentityController.class);

    private final InterfaceSecurityService interfaceSecurityService;
    private final SignInFailureLimitedStampManager signInFailureLimitedStampManager;

    public IdentityController(
            InterfaceSecurityService interfaceSecurityService,
            SignInFailureLimitedStampManager signInFailureLimitedStampManager) {
        this.interfaceSecurityService = interfaceSecurityService;
        this.signInFailureLimitedStampManager = signInFailureLimitedStampManager;
    }

    @Operation(
            summary = "获取后台加密公钥",
            description = "根据未登录时的身份标识，在后台创建RSA公钥和私钥。身份标识为前端的唯一标识，如果为空，则在后台创建一个",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            content = @Content(mediaType = "application/json")),
            responses = {
                @ApiResponse(
                        description = "自定义Session",
                        content = @Content(mediaType = "application/json"))
            })
    @Parameters({
        @Parameter(
                name = "sessionCreate",
                required = true,
                description = "Session创建请求参数",
                schema = @Schema(implementation = SessionCreate.class)),
    })
    @PostMapping("/open/identity/session")
    public Result<Session> codeToSession(@Validated @RequestBody SessionCreate sessionCreate) {

        SecretKey secretKey =
                interfaceSecurityService.createSecretKey(
                        sessionCreate.getClientId(),
                        sessionCreate.getClientSecret(),
                        sessionCreate.getSessionId());
        if (ObjectUtils.isNotEmpty(secretKey)) {
            Session session = new Session();
            session.setSessionId(secretKey.getIdentity());
            session.setPublicKey(secretKey.getPublicKey());
            session.setState(secretKey.getState());

            return Result.success(session);
        }

        return null;
        //        return Result.fail();
    }

    @Operation(
            summary = "获取AES秘钥",
            description = "用后台publicKey，加密前台publicKey，到后台换取AES秘钥",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            content = @Content(mediaType = "application/json")),
            responses = {
                @ApiResponse(
                        description = "加密后的AES",
                        content = @Content(mediaType = "application/json"))
            })
    @Parameters({
        @Parameter(
                name = "sessionExchange",
                required = true,
                description = "秘钥交换",
                schema = @Schema(implementation = SessionExchange.class)),
    })
    @PostMapping("/open/identity/exchange")
    public Result<String> exchange(@Validated @RequestBody SessionExchange sessionExchange) {

        String encryptedAesKey =
                interfaceSecurityService.exchange(
                        sessionExchange.getSessionId(), sessionExchange.getConfidential());
        if (StringUtils.isNotEmpty(encryptedAesKey)) {
            return Result.success(encryptedAesKey);
        }

        return Result.fail();
    }

    //    @Crypto(responseEncrypt = false)
    @Operation(
            summary = "获取登录出错剩余次数",
            description = "获取登录出错剩余次数",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            content = @Content(mediaType = "application/json")),
            responses = {
                @ApiResponse(
                        description = "加密后的AES",
                        content = @Content(mediaType = "application/json"))
            })
    @Parameters({
        @Parameter(
                name = "signInErrorPrompt",
                required = true,
                description = "提示信息所需参数",
                schema = @Schema(implementation = SignInErrorPrompt.class)),
    })
    @PostMapping("/open/identity/prompt")
    public Result<SignInErrorStatus> prompt(
            @Validated @RequestBody SignInErrorPrompt signInErrorPrompt) {
        SignInErrorStatus signInErrorStatus =
                signInFailureLimitedStampManager.errorStatus(signInErrorPrompt.getUsername());
        return Result.success(signInErrorStatus);
    }
}
