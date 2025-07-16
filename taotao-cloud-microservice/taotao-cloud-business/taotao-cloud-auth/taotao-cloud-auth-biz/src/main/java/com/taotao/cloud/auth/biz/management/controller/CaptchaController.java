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

import com.taotao.boot.captcha.support.core.dto.Captcha;
import com.taotao.boot.captcha.support.core.dto.Verification;
import com.taotao.boot.captcha.support.core.processor.CaptchaRendererFactory;
import com.taotao.boot.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>验证码Controller </p>
 *
 *
 * @since : 2021/12/12 10:44
 */
@RestController
@RequestMapping("/open/captcha")
@Validated
@Tags({@Tag(name = "OAuth2 认证服务器接口"), @Tag(name = "OAuth2 认证服务器开放接口"), @Tag(name = "验证码接口")})
public class CaptchaController {

    @Autowired private CaptchaRendererFactory captchaRendererFactory;

    //    @AccessLimited
    @Operation(
            summary = "获取验证码",
            description = "通过传递身份信息（类似于Session标识）",
            responses = {
                @ApiResponse(
                        description = "验证码图形信息",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Map.class)))
            })
    @Parameters({
        @Parameter(name = "identity", required = true, in = ParameterIn.PATH, description = "身份信息"),
        @Parameter(name = "category", required = true, in = ParameterIn.PATH, description = "验证码类型")
    })
    @GetMapping
    public Result<Captcha> create(
            @NotBlank(message = "身份信息不能为空") String identity,
            @NotBlank(message = "验证码类型不能为空") String category) {
        Captcha captcha = captchaRendererFactory.getCaptcha(identity, category);
        //        if (ObjectUtils.isNotEmpty(captcha)) {
        //            return Result.success("验证码创建成功", captcha);
        //        } else {
        //            return Result.failure("验证码创建失败");
        //        }
        return Result.success(captcha);
    }

    //    @Idempotent
    //    @Crypto(responseEncrypt = false)
    @Operation(
            summary = "验证码验证",
            description = "验证验证码返回数据是否正确。使用加密信息",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            content = @Content(mediaType = "application/json")),
            responses = {
                @ApiResponse(
                        description = "验证结果",
                        content = @Content(mediaType = "application/json"))
            })
    @Parameters({
        @Parameter(
                name = "jigsawVerification",
                required = true,
                description = "验证码验证参数",
                schema = @Schema(implementation = Verification.class))
    })
    @PostMapping
    public Result<Boolean> check(@Valid @RequestBody Verification verification) {
        boolean isSuccess = captchaRendererFactory.verify(verification);
        //        if (isSuccess) {
        //            return Result.success("验证码验证成功", true);
        //        }
        //        return Result.failure("验证码验证失败", true);

        return Result.success(true);
    }
}
