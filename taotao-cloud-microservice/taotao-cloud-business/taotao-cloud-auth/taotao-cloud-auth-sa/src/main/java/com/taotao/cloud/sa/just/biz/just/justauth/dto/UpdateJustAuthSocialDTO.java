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

package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import io.swagger.annotations.ApiModel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 第三方用户信息
 *
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "JustAuthSocial对象", description = "第三方用户信息")
public class UpdateJustAuthSocialDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "第三方ID")
    @NotBlank(message = "第三方ID不能为空")
    @Length(min = 1, max = 100)
    private String uuid;

    @Schema(description = "第三方来源")
    @NotBlank(message = "第三方来源不能为空")
    @Length(min = 1, max = 32)
    private String source;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(min = 1, max = 64)
    private String username;

    @Schema(description = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    @Length(min = 1, max = 64)
    private String nickname;

    @Schema(description = "用户头像")
    @NotBlank(message = "用户头像不能为空")
    @Length(min = 1, max = 500)
    private String avatar;

    @Schema(description = "用户网址")
    @NotBlank(message = "用户网址不能为空")
    @Length(min = 1, max = 500)
    private String blog;

    @Schema(description = "所在公司")
    @NotBlank(message = "所在公司不能为空")
    @Length(min = 1, max = 255)
    private String company;

    @Schema(description = "位置")
    @NotBlank(message = "位置不能为空")
    @Length(min = 1, max = 100)
    private String location;

    @Schema(description = "用户邮箱")
    @NotBlank(message = "用户邮箱不能为空")
    @Length(min = 1, max = 100)
    private String email;

    @Schema(description = "用户备注")
    @NotBlank(message = "用户备注不能为空")
    @Length(min = 1, max = 500)
    private String remark;

    @Schema(description = "性别")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min = 1, max = 3)
    private Integer gender;

    @Schema(description = "授权令牌")
    @NotBlank(message = "授权令牌不能为空")
    @Length(min = 1, max = 500)
    private String accessToken;

    @Schema(description = "令牌有效期")
    @NotBlank(message = "令牌有效期不能为空")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min = 1, max = 10)
    private Integer expireIn;

    @Schema(description = "刷新令牌")
    @NotBlank(message = "刷新令牌不能为空")
    @Length(min = 1, max = 500)
    private String refreshToken;

    @Schema(description = "刷新令牌有效期")
    @NotBlank(message = "刷新令牌有效期不能为空")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min = 1, max = 10)
    private Integer accessTokenExpireIn;

    @Schema(description = "第三方用户ID")
    @NotBlank(message = "第三方用户ID不能为空")
    @Length(min = 1, max = 100)
    private String uid;

    @Schema(description = "第三方用户OpenId")
    @NotBlank(message = "第三方用户OpenId不能为空")
    @Length(min = 1, max = 100)
    private String openId;

    @Schema(description = "AccessCode")
    @NotBlank(message = "AccessCode不能为空")
    @Length(min = 1, max = 255)
    private String accessCode;

    @Schema(description = "第三方用户UnionId")
    @NotBlank(message = "第三方用户UnionId不能为空")
    @Length(min = 1, max = 255)
    private String unionId;

    @Schema(description = "Google Scope")
    @NotBlank(message = "Google Scope不能为空")
    @Length(min = 1, max = 255)
    private String scope;

    @Schema(description = "Google TokenType")
    @NotBlank(message = "Google TokenType不能为空")
    @Length(min = 1, max = 255)
    private String tokenType;

    @Schema(description = "Google IdToken")
    @NotBlank(message = "Google IdToken不能为空")
    @Length(min = 1, max = 255)
    private String idToken;

    @Schema(description = "小米MacAlgorithm")
    @NotBlank(message = "小米MacAlgorithm不能为空")
    @Length(min = 1, max = 255)
    private String macAlgorithm;

    @Schema(description = "小米Mac_Key")
    @NotBlank(message = "小米Mac_Key不能为空")
    @Length(min = 1, max = 255)
    private String macKey;

    @Schema(description = "企业微信code")
    @NotBlank(message = "企业微信code不能为空")
    @Length(min = 1, max = 255)
    private String code;

    @Schema(description = "Twitter OauthToken")
    @NotBlank(message = "Twitter OauthToken不能为空")
    @Length(min = 1, max = 255)
    private String oauthToken;

    @Schema(description = "Twitter OauthTokenSecret")
    @NotBlank(message = "Twitter OauthTokenSecret不能为空")
    @Length(min = 1, max = 255)
    private String oauthTokenSecret;

    @Schema(description = "Twitter UserId")
    @NotBlank(message = "Twitter UserId不能为空")
    @Length(min = 1, max = 100)
    private String userId;

    @Schema(description = "Twitter ScreenName")
    @NotBlank(message = "Twitter ScreenName不能为空")
    @Length(min = 1, max = 255)
    private String screenName;

    @Schema(description = "Twitter OauthCallbackConfirmed")
    @NotBlank(message = "Twitter OauthCallbackConfirmed不能为空")
    @Length(min = 1, max = 1)
    private Boolean oauthCallbackConfirmed;

    @Schema(description = "原始用户信息")
    @NotBlank(message = "原始用户信息不能为空")
    @Length(min = 1, max = 65535)
    private String rawUserInfo;
}
