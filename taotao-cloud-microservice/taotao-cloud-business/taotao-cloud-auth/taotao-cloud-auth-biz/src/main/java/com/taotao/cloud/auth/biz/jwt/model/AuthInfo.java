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

package com.taotao.cloud.auth.biz.jwt.model;

import java.time.LocalDateTime;

/** 认证信息 */
public class AuthInfo {

    // 令牌
    private String token;
    // 令牌类型
    private String tokenType;
    // 刷新令牌
    private String refreshToken;
    // 用户名
    private String name;
    // 账号名
    private String account;
    // 头像
    private String avatar;
    // 工作描述
    private String workDescribe;
    // 用户id
    private Long userId;
    // 过期时间（秒）
    private long expire;
    // 到期时间
    private LocalDateTime expiration;
    // 有效期
    private Long expireMillis;

    public AuthInfo() {}

    public AuthInfo(
            String token,
            String tokenType,
            String refreshToken,
            String name,
            String account,
            String avatar,
            String workDescribe,
            Long userId,
            long expire,
            LocalDateTime expiration,
            Long expireMillis) {
        this.token = token;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.name = name;
        this.account = account;
        this.avatar = avatar;
        this.workDescribe = workDescribe;
        this.userId = userId;
        this.expire = expire;
        this.expiration = expiration;
        this.expireMillis = expireMillis;
    }

    public String getToken() {
        return token;
    }

    public AuthInfo setToken(String token) {
        this.token = token;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public AuthInfo setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AuthInfo setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getName() {
        return name;
    }

    public AuthInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public AuthInfo setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public AuthInfo setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getWorkDescribe() {
        return workDescribe;
    }

    public AuthInfo setWorkDescribe(String workDescribe) {
        this.workDescribe = workDescribe;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public AuthInfo setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public long getExpire() {
        return expire;
    }

    public AuthInfo setExpire(long expire) {
        this.expire = expire;
        return this;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public AuthInfo setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
        return this;
    }

    public Long getExpireMillis() {
        return expireMillis;
    }

    public AuthInfo setExpireMillis(Long expireMillis) {
        this.expireMillis = expireMillis;
        return this;
    }
}
