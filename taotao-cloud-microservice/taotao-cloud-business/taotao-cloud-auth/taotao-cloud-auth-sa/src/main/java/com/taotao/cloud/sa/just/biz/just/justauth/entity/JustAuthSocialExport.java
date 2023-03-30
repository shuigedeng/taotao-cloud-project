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

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.localdatetime.LocalDateTimeStringConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 第三方用户信息
 *
 * @since 2022-05-23
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@Schema(description = "第三方用户信息数据导出")
public class JustAuthSocialExport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "第三方ID")
    @ExcelProperty(value = "第三方ID", index = 0)
    @ColumnWidth(20)
    private String uuid;

    @Schema(description = "第三方来源")
    @ExcelProperty(value = "第三方来源", index = 1)
    @ColumnWidth(20)
    private String source;

    @Schema(description = "用户名")
    @ExcelProperty(value = "用户名", index = 2)
    @ColumnWidth(20)
    private String username;

    @Schema(description = "用户昵称")
    @ExcelProperty(value = "用户昵称", index = 3)
    @ColumnWidth(20)
    private String nickname;

    @Schema(description = "用户头像")
    @ExcelProperty(value = "用户头像", index = 4)
    @ColumnWidth(20)
    private String avatar;

    @Schema(description = "用户网址")
    @ExcelProperty(value = "用户网址", index = 5)
    @ColumnWidth(20)
    private String blog;

    @Schema(description = "所在公司")
    @ExcelProperty(value = "所在公司", index = 6)
    @ColumnWidth(20)
    private String company;

    @Schema(description = "位置")
    @ExcelProperty(value = "位置", index = 7)
    @ColumnWidth(20)
    private String location;

    @Schema(description = "用户邮箱")
    @ExcelProperty(value = "用户邮箱", index = 8)
    @ColumnWidth(20)
    private String email;

    @Schema(description = "用户备注")
    @ExcelProperty(value = "用户备注", index = 9)
    @ColumnWidth(20)
    private String remark;

    @Schema(description = "性别")
    @ExcelProperty(value = "性别", index = 10)
    @ColumnWidth(20)
    private Integer gender;

    @Schema(description = "授权令牌")
    @ExcelProperty(value = "授权令牌", index = 11)
    @ColumnWidth(20)
    private String accessToken;

    @Schema(description = "令牌有效期")
    @ExcelProperty(value = "令牌有效期", index = 12)
    @ColumnWidth(20)
    private Integer expireIn;

    @Schema(description = "刷新令牌")
    @ExcelProperty(value = "刷新令牌", index = 13)
    @ColumnWidth(20)
    private String refreshToken;

    @Schema(description = "刷新令牌有效期")
    @ExcelProperty(value = "刷新令牌有效期", index = 14)
    @ColumnWidth(20)
    private Integer accessTokenExpireIn;

    @Schema(description = "第三方用户ID")
    @ExcelProperty(value = "第三方用户ID", index = 15)
    @ColumnWidth(20)
    private String uid;

    @Schema(description = "第三方用户OpenId")
    @ExcelProperty(value = "第三方用户OpenId", index = 16)
    @ColumnWidth(20)
    private String openId;

    @Schema(description = "AccessCode")
    @ExcelProperty(value = "AccessCode", index = 17)
    @ColumnWidth(20)
    private String accessCode;

    @Schema(description = "第三方用户UnionId")
    @ExcelProperty(value = "第三方用户UnionId", index = 18)
    @ColumnWidth(20)
    private String unionId;

    @Schema(description = "Google Scope")
    @ExcelProperty(value = "Google Scope", index = 19)
    @ColumnWidth(20)
    private String scope;

    @Schema(description = "Google TokenType")
    @ExcelProperty(value = "Google TokenType", index = 20)
    @ColumnWidth(20)
    private String tokenType;

    @Schema(description = "Google IdToken")
    @ExcelProperty(value = "Google IdToken", index = 21)
    @ColumnWidth(20)
    private String idToken;

    @Schema(description = "小米MacAlgorithm")
    @ExcelProperty(value = "小米MacAlgorithm", index = 22)
    @ColumnWidth(20)
    private String macAlgorithm;

    @Schema(description = "小米Mac_Key")
    @ExcelProperty(value = "小米Mac_Key", index = 23)
    @ColumnWidth(20)
    private String macKey;

    @Schema(description = "企业微信code")
    @ExcelProperty(value = "企业微信code", index = 24)
    @ColumnWidth(20)
    private String code;

    @Schema(description = "Twitter OauthToken")
    @ExcelProperty(value = "Twitter OauthToken", index = 25)
    @ColumnWidth(20)
    private String oauthToken;

    @Schema(description = "Twitter OauthTokenSecret")
    @ExcelProperty(value = "Twitter OauthTokenSecret", index = 26)
    @ColumnWidth(20)
    private String oauthTokenSecret;

    @Schema(description = "Twitter UserId")
    @ExcelProperty(value = "Twitter UserId", index = 27)
    @ColumnWidth(20)
    private String userId;

    @Schema(description = "Twitter ScreenName")
    @ExcelProperty(value = "Twitter ScreenName", index = 28)
    @ColumnWidth(20)
    private String screenName;

    @Schema(description = "Twitter OauthCallbackConfirmed")
    @ExcelProperty(value = "Twitter OauthCallbackConfirmed", index = 29)
    @ColumnWidth(20)
    private Boolean oauthCallbackConfirmed;

    @Schema(description = "原始用户信息")
    @ExcelProperty(value = "原始用户信息", index = 30)
    @ColumnWidth(20)
    private String rawUserInfo;

    @Schema(description = "创建时间")
    @ExcelProperty(value = "创建时间", index = 31, converter = LocalDateTimeStringConverter.class)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
