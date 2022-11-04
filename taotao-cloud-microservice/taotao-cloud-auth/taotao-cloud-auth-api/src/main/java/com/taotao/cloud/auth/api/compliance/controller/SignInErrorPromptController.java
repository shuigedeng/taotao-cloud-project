//
// package com.taotao.cloud.auth.api.compliance.controller;
//
// import cn.herodotus.engine.assistant.core.domain.Result;
// import cn.herodotus.engine.oauth2.compliance.dto.SignInErrorPrompt;
// import cn.herodotus.engine.oauth2.compliance.dto.SignInErrorStatus;
// import cn.herodotus.engine.oauth2.compliance.stamp.SignInFailureLimitedStampManager;
// import cn.herodotus.engine.protect.core.annotation.Crypto;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.Parameters;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import io.swagger.v3.oas.annotations.tags.Tags;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * @author gengwei.zheng
//  * @see <a href="https://conkeyn.iteye.com/blog/2296406">参考文档</a>
//  */
// @RestController
// @Tags({
//         @Tag(name = "OAuth2 认证服务接口"),
//         @Tag(name = "OAuth2 应用安全合规接口"),
//         @Tag(name = "OAuth2 登录错误提示接口")
// })
// public class SignInErrorPromptController {
//
//     private final Logger log = LoggerFactory.getLogger(SignInErrorPromptController.class);
//
//     private final SignInFailureLimitedStampManager signInFailureLimitedStampManager;
//
//     @Autowired
//     public SignInErrorPromptController(SignInFailureLimitedStampManager signInFailureLimitedStampManager) {
//         this.signInFailureLimitedStampManager = signInFailureLimitedStampManager;
//     }
//
//     @Crypto(responseEncrypt = false)
//     @Operation(summary = "获取登录出错剩余次数", description = "获取登录出错剩余次数",
//             requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
//             responses = {@ApiResponse(description = "加密后的AES", content = @Content(mediaType = "application/json"))})
//     @Parameters({
//             @Parameter(name = "signInErrorPrompt", required = true, description = "提示信息所需参数", schema = @Schema(implementation = SignInErrorPrompt.class)),
//     })
//     @PostMapping("/open/identity/prompt")
//     public Result<SignInErrorStatus> prompt(@Validated @RequestBody SignInErrorPrompt signInErrorPrompt) {
//         SignInErrorStatus signInErrorStatus = signInFailureLimitedStampManager.errorStatus(signInErrorPrompt.getUsername());
//         return Result.content(signInErrorStatus);
//     }
// }
