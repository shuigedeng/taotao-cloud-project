//
// package com.taotao.cloud.auth.api.compliance.dto;
//
// import cn.herodotus.engine.rest.core.definition.dto.BaseDto;
// import io.swagger.v3.oas.annotations.media.Schema;
//
// import javax.validation.constraints.NotBlank;
//
// /**
//  * <p>Description: 登录提示信息 </p>
//  *
//  * @author : gengwei.zheng
//  * @date : 2022/7/8 20:52
//  */
// @Schema(title = "登录错误提示信息")
// public class SignInErrorPrompt extends BaseDto {
//
//     @NotBlank(message = "登录用户名不能为空")
//     @Schema(name = "登录用户名", title = "必须是有效的用户名")
//     private String username;
//
//     public String getUsername() {
//         return username;
//     }
//
//     public void setUsername(String username) {
//         this.username = username;
//     }
// }
