/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.compliance.controller;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.oauth2.compliance.dto.SignInErrorPrompt;
import cn.herodotus.engine.oauth2.compliance.dto.SignInErrorStatus;
import cn.herodotus.engine.oauth2.compliance.stamp.SignInFailureLimitedStampManager;
import cn.herodotus.engine.protect.core.annotation.Crypto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gengwei.zheng
 * @see <a href="https://conkeyn.iteye.com/blog/2296406">参考文档</a>
 */
@RestController
@Tags({
        @Tag(name = "OAuth2 认证服务接口"),
        @Tag(name = "OAuth2 应用安全合规接口"),
        @Tag(name = "OAuth2 登录错误提示接口")
})
public class SignInErrorPromptController {

    private final Logger log = LoggerFactory.getLogger(SignInErrorPromptController.class);

    private final SignInFailureLimitedStampManager signInFailureLimitedStampManager;

    @Autowired
    public SignInErrorPromptController(SignInFailureLimitedStampManager signInFailureLimitedStampManager) {
        this.signInFailureLimitedStampManager = signInFailureLimitedStampManager;
    }

    @Crypto(responseEncrypt = false)
    @Operation(summary = "获取登录出错剩余次数", description = "获取登录出错剩余次数",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "加密后的AES", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "signInErrorPrompt", required = true, description = "提示信息所需参数", schema = @Schema(implementation = SignInErrorPrompt.class)),
    })
    @PostMapping("/open/identity/prompt")
    public Result<SignInErrorStatus> prompt(@Validated @RequestBody SignInErrorPrompt signInErrorPrompt) {
        SignInErrorStatus signInErrorStatus = signInFailureLimitedStampManager.errorStatus(signInErrorPrompt.getUsername());
        return Result.content(signInErrorStatus);
    }
}
