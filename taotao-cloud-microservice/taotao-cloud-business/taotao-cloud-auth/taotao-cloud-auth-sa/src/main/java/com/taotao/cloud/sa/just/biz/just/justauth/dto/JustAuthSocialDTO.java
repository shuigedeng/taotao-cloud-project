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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 第三方用户信息
 *
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "JustAuthSocialDTO对象", description = "第三方用户信息")
public class JustAuthSocialDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "第三方ID")
    private String uuid;

    @Schema(description = "第三方来源")
    private String source;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户网址")
    private String blog;

    @Schema(description = "所在公司")
    private String company;

    @Schema(description = "位置")
    private String location;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户备注")
    private String remark;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "授权令牌")
    private String accessToken;

    @Schema(description = "令牌有效期")
    private Integer expireIn;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "刷新令牌有效期")
    private Integer accessTokenExpireIn;

    @Schema(description = "第三方用户ID")
    private String uid;

    @Schema(description = "第三方用户OpenId")
    private String openId;

    @Schema(description = "AccessCode")
    private String accessCode;

    @Schema(description = "第三方用户UnionId")
    private String unionId;

    @Schema(description = "Google Scope")
    private String scope;

    @Schema(description = "Google TokenType")
    private String tokenType;

    @Schema(description = "Google IdToken")
    private String idToken;

    @Schema(description = "小米MacAlgorithm")
    private String macAlgorithm;

    @Schema(description = "小米Mac_Key")
    private String macKey;

    @Schema(description = "企业微信code")
    private String code;

    @Schema(description = "Twitter OauthToken")
    private String oauthToken;

    @Schema(description = "Twitter OauthTokenSecret")
    private String oauthTokenSecret;

    @Schema(description = "Twitter UserId")
    private String userId;

    @Schema(description = "Twitter ScreenName")
    private String screenName;

    @Schema(description = "Twitter OauthCallbackConfirmed")
    private Boolean oauthCallbackConfirmed;

    @Schema(description = "原始用户信息")
    private String rawUserInfo;
}
