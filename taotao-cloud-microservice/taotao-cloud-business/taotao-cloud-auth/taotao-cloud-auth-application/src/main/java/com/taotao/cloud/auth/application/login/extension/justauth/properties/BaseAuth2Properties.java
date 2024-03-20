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

package com.taotao.cloud.auth.application.login.extension.justauth.properties;

import com.xkcoding.http.config.HttpConfig;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.AuthScope;

/**
 * OAuth2 基本属性
 *
 * @author YongWu zheng
 * @version V1.0  Created by 2020/5/17 14:08
 */
@SuppressWarnings("jol")
@Getter
@Setter
public class BaseAuth2Properties {

    private String clientId;
    private String clientSecret;
    /**
     * 使用 Coding 登录时，需要传该值。
     * <p>
     * 团队域名前缀，比如以“ https://justauth.coding.net/ ”为例，{@code codingGroupName} = justauth
     *
     * @since 1.15.5
     */
    private String codingGroupName;
    /**
     * 支付宝公钥：当选择支付宝登录时，该值可用
     * 对应“RSA2(SHA256)密钥”中的“支付宝公钥”
     */
    private String alipayPublicKey;
    /**
     * 支付宝: 支付宝有自己的代理, 默认代理对支付宝不生效, 代理主机:
     *
     * @since justAuth 1.15.9
     */
    private String proxyHost;
    /**
     * 支付宝: 支付宝有自己的代理, 默认代理对支付宝不生效, 代理端口:
     *
     * @since justAuth 1.15.9
     */
    private Integer proxyPort;
    /**
     * 是否需要申请 unionId，默认: false. 目前只针对qq登录
     * 注：qq授权登录时，获取 unionId 需要单独发送邮件申请权限。如果个人开发者账号中申请了该权限，可以将该值置为true，在获取openId时就会同步获取unionId
     * 参考链接：http://wiki.connect.qq.com/unionid%E4%BB%8B%E7%BB%8D
     * <p>
     * 1.7.1版本新增参数
     */
    private Boolean unionId = false;
    /**
     * Stack Overflow Key
     * <p>
     *
     * @since 1.9.0
     */
    private String stackOverflowKey;
    /**
     * 企业微信，授权方的网页应用ID
     *
     * @since 1.10.0
     */
    private String agentId;
    /**
     * 自定义第三方授权登录, 当 {@code Auth2Properties#customize} 时有效, 此字段必须以驼峰方式命名.
     * 比如此字段的值为 umsCustomize, 那么 /auth2/authorization/customize 会替换为 /auth2/authorization/umsCustomize
     */
    private String customizeProviderId;

    /**
     * 自定义第三方授权登录, 当 {@code Auth2Properties#customize} 时有效, 设置第三方是否在国外, 默认: false.
     * 如果为 false 时, 设置 {@link HttpConfig} 的超时时间为 ums.oauth.proxy.timeout 的值.
     * 如果为 true 时, 设置 {@link HttpConfig} 的超时时间为 ums.oauth.proxy.foreignTimeout 的值.
     */
    private Boolean customizeIsForeign = Boolean.FALSE;
    /**
     * 喜马拉雅：设备ID, 设备唯一标识ID
     *
     * @since justAuth 1.15.8
     */
    private String deviceId;

    /**
     * 喜马拉雅：客户端操作系统类型，1-iOS系统，2-Android系统，3-Web
     *
     * @since justAuth 1.15.9
     */
    private Integer clientOsType;

    /**
     * 喜马拉雅：客户端包名，如果 {@link AuthConfig#getClientOsType()} 为1或2时必填。对Android客户端是包名，对IOS客户端是Bundle ID
     *
     * @since justAuth 1.15.9
     */
    private String packId;

    /**
     * 支持自定义授权平台的 scope 内容, 格式参考对应的 {@link AuthScope#getScope()} 的子类.
     * 注意: 会自动添加默认的 scope 设置.
     *
     * @since 1.15.7
     */
    private List<String> scopes;
}
