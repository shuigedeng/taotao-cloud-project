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

package com.taotao.cloud.auth.biz.authentication.federation;

import java.time.LocalDateTime;
import java.util.Collection;
import lombok.Data;
import lombok.experimental.*;

/**
 * oauth2登录后获取用户信息响应类
 */
@Data
public class Oauth2UserinfoResult {

    /**
     * 自增id
     */
    private Integer id;

    /**
     * 用户名、昵称
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 用户来源
     */
    private String sourceFrom;

    /**
     * 权限信息
     */
    private Collection<CustomGrantedAuthority> authorities;

    /**
     * 地址
     */
    private String location;

    /**
     * 三方登录用户名
     */
    private String thirdUsername;

    /**
     * 三方登录获取的认证信息
     */
    private String credentials;

    /**
     * 三方登录获取的认证信息的过期时间
     */
    private LocalDateTime credentialsExpiresAt;
}
