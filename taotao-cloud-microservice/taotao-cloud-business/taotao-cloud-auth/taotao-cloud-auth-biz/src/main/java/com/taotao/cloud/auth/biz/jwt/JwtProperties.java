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

package com.taotao.cloud.auth.biz.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** 认证服务端 属性 */
@ConfigurationProperties(prefix = JwtProperties.PREFIX)
public class JwtProperties {

    public static final String PREFIX = "lamp.authentication";

    /** 过期时间 2h 单位：s */
    private Long expire = 7200L;

    /** 刷新token的过期时间 8h 单位：s */
    private Long refreshExpire = 28800L;

    /** 设置解析token时，允许的误差 单位：s 使用场景1：多台服务器集群部署时，服务器时间戳可能不一致 使用场景2：？ */
    private Long allowedClockSkewSeconds = 60L;

    public JwtProperties() {}

    public JwtProperties(Long expire, Long refreshExpire, Long allowedClockSkewSeconds) {
        this.expire = expire;
        this.refreshExpire = refreshExpire;
        this.allowedClockSkewSeconds = allowedClockSkewSeconds;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Long getRefreshExpire() {
        return refreshExpire;
    }

    public void setRefreshExpire(Long refreshExpire) {
        this.refreshExpire = refreshExpire;
    }

    public Long getAllowedClockSkewSeconds() {
        return allowedClockSkewSeconds;
    }

    public void setAllowedClockSkewSeconds(Long allowedClockSkewSeconds) {
        this.allowedClockSkewSeconds = allowedClockSkewSeconds;
    }
}
