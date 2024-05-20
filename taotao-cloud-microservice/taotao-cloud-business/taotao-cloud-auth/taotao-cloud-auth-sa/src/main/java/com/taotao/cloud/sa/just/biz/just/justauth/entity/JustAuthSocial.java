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

package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.data.mybatis.base.entity.MpSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 第三方用户信息
 *
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_just_auth_social")
@Schema(description = "第三方用户信息")
public class JustAuthSocial extends MpSuperEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "第三方系统的唯一ID	")
    @TableField("uuid")
    private String uuid;

    @Schema(description = "第三方用户来源")
    @TableField("source")
    private String source;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "用户头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "用户网址")
    @TableField("blog")
    private String blog;

    @Schema(description = "所在公司")
    @TableField("company")
    private String company;

    @Schema(description = "位置")
    @TableField("location")
    private String location;

    @Schema(description = "用户邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "用户备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "性别 -1未知 1男 0女")
    @TableField("gender")
    private Integer gender;

    @Schema(description = "用户的授权令牌")
    @TableField("access_token")
    private String accessToken;

    @Schema(description = "第三方用户的授权令牌的有效期")
    @TableField("expire_in")
    private Integer expireIn;

    @Schema(description = "刷新令牌")
    @TableField("refresh_token")
    private String refreshToken;

    @Schema(description = "第三方刷新令牌的有效期")
    @TableField("access_token_expire_in")
    private Integer accessTokenExpireIn;

    @Schema(description = "第三方用户的 ID")
    @TableField("uid")
    private String uid;

    @Schema(description = "第三方用户的 open id")
    @TableField("open_id")
    private String openId;

    @Schema(description = "个别平台的授权信息")
    @TableField("access_code")
    private String accessCode;

    @Schema(description = "第三方用户的 union id")
    @TableField("union_id")
    private String unionId;

    @Schema(description = "Google Scope")
    @TableField("scope")
    private String scope;

    @Schema(description = "Google TokenType")
    @TableField("token_type")
    private String tokenType;

    @Schema(description = "Google IdToken")
    @TableField("id_token")
    private String idToken;

    @Schema(description = "小米MacAlgorithm")
    @TableField("mac_algorithm")
    private String macAlgorithm;

    @Schema(description = "小米Mac_Key")
    @TableField("mac_key")
    private String macKey;

    @Schema(description = "企业微信code")
    @TableField("code")
    private String code;

    @Schema(description = "Twitter OauthToken")
    @TableField("oauth_token")
    private String oauthToken;

    @Schema(description = "Twitter OauthTokenSecret")
    @TableField("oauth_token_secret")
    private String oauthTokenSecret;

    @Schema(description = "Twitter UserId")
    @TableField("user_id")
    private String userId;

    @Schema(description = "Twitter ScreenName")
    @TableField("screen_name")
    private String screenName;

    @Schema(description = "Twitter OauthCallbackConfirmed")
    @TableField("oauth_callback_confirmed")
    private Boolean oauthCallbackConfirmed;

    @Schema(description = "原始用户信息")
    @TableField("rawUserInfo")
    private String rawUserInfo;
}
