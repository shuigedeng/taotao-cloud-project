/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.uaa.other;

import com.taotao.cloud.common.constant.SymbolConstants;

/**
 * <p>Description: 基础共用常量值常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/13 21:18
 */
public interface BaseConstants {

    String NONE = "none";
    String CODE = "code";

    String DEFAULT_TENANT_ID = "public";
    String DEFAULT_TREE_ROOT_ID = "0";

    /* ---------- 配置属性通用常量 ---------- */

    String PROPERTY_ENABLED = ".enabled";
    String PROPERTY_PREFIX_SPRING = "spring";
    String PROPERTY_PREFIX_FEIGN = "feign";
    String PROPERTY_PREFIX_HERODOTUS = "herodotus";

    String PROPERTY_SPRING_CLOUD = PROPERTY_PREFIX_SPRING + ".cloud";
    String PROPERTY_SPRING_JPA = PROPERTY_PREFIX_SPRING + ".jpa";
    String PROPERTY_SPRING_REDIS = PROPERTY_PREFIX_SPRING + ".redis";

    String ANNOTATION_PREFIX = "${";
    String ANNOTATION_SUFFIX = "}";

    /* ---------- Herodotus 自定义配置属性 ---------- */
    String PROPERTY_PREFIX_ACCESS = PROPERTY_PREFIX_HERODOTUS + ".access";
    String PROPERTY_PREFIX_CACHE = PROPERTY_PREFIX_HERODOTUS + ".cache";
    String PROPERTY_PREFIX_CAPTCHA = PROPERTY_PREFIX_HERODOTUS + ".captcha";
    String PROPERTY_PREFIX_CRYPTO = PROPERTY_PREFIX_HERODOTUS + ".crypto";
    String PROPERTY_PREFIX_ENDPOINT = PROPERTY_PREFIX_HERODOTUS + ".endpoint";
    String PROPERTY_PREFIX_EVENT = PROPERTY_PREFIX_HERODOTUS + ".event";
    String PROPERTY_PREFIX_LOG_CENTER = PROPERTY_PREFIX_HERODOTUS + ".log-center";
    String PROPERTY_PREFIX_MANAGE = PROPERTY_PREFIX_HERODOTUS + ".manage";
    String PROPERTY_PREFIX_MESSAGE = PROPERTY_PREFIX_HERODOTUS + ".message";
    String PROPERTY_PREFIX_MULTI_TENANT = PROPERTY_PREFIX_HERODOTUS + ".multi-tenant";
    String PROPERTY_PREFIX_NOSQL = PROPERTY_PREFIX_HERODOTUS + ".nosql";
    String PROPERTY_PREFIX_OAUTH2 = PROPERTY_PREFIX_HERODOTUS + ".oauth2";
    String PROPERTY_PREFIX_OSS = PROPERTY_PREFIX_HERODOTUS + ".oss";
    String PROPERTY_PREFIX_PAY = PROPERTY_PREFIX_HERODOTUS + ".pay";
    String PROPERTY_PREFIX_PLATFORM = PROPERTY_PREFIX_HERODOTUS + ".platform";
    String PROPERTY_PREFIX_REST = PROPERTY_PREFIX_HERODOTUS + ".rest";
    String PROPERTY_PREFIX_SECURE = PROPERTY_PREFIX_HERODOTUS + ".secure";
    String PROPERTY_PREFIX_SMS = PROPERTY_PREFIX_HERODOTUS + ".sms";
    String PROPERTY_PREFIX_SWAGGER = PROPERTY_PREFIX_HERODOTUS + ".swagger";

    /* ---------- Spring 家族配置属性 ---------- */

    String ITEM_SWAGGER_ENABLED = PROPERTY_PREFIX_SWAGGER + PROPERTY_ENABLED;
    String ITEM_SPRING_APPLICATION_NAME = PROPERTY_PREFIX_SPRING + ".application.name";

    String ANNOTATION_APPLICATION_NAME = ANNOTATION_PREFIX + ITEM_SPRING_APPLICATION_NAME + ANNOTATION_SUFFIX;


    /* ---------- 通用缓存常量 ---------- */

    String CACHE_PREFIX = "cache:";
    String CACHE_SIMPLE_BASE_PREFIX = CACHE_PREFIX + "simple:";
    String CACHE_TOKEN_BASE_PREFIX = CACHE_PREFIX + "token:";

    String AREA_PREFIX = "data:";


    /* ---------- Oauth2 和 Security 通用缓存常量 ---------- */

    /**
     * Oauth2 模式类型
     */
    String PASSWORD = "password";
    String SOCIAL_CREDENTIALS = "social_credentials";

    String DEFAULT_AUTHORIZATION_ENDPOINT = "/oauth2/authorize";
    String DEFAULT_TOKEN_ENDPOINT = "/oauth2/token";
    String DEFAULT_JWK_SET_ENDPOINT = "/oauth2/jwks";
    String DEFAULT_TOKEN_REVOCATION_ENDPOINT = "/oauth2/revoke";
    String DEFAULT_TOKEN_INTROSPECTION_ENDPOINT = "/oauth2/introspect";
    String DEFAULT_DEVICE_AUTHORIZATION_ENDPOINT = "/oauth2/device_authorization";
    String DEFAULT_DEVICE_VERIFICATION_ENDPOINT = "/oauth2/device_verification";
    String DEFAULT_OIDC_CLIENT_REGISTRATION_ENDPOINT = "/connect/register";
    String DEFAULT_OIDC_LOGOUT_ENDPOINT = "/connect/logout";
    String DEFAULT_OIDC_USER_INFO_ENDPOINT = "/userinfo";

    String CUSTOM_AUTHORIZATION_CONSENT_URI = "/oauth2/consent";
    String CUSTOM_DEVICE_ACTIVATION_URI = "/oauth2/device_activation";
    String CUSTOM_DEVICE_VERIFICATION_SUCCESS_URI = "/device_activated";

    String OPEN_API_SECURITY_SCHEME_BEARER_NAME = "HERODOTUS_AUTH";

    String BEARER_TYPE = "Bearer";
    String BEARER_TOKEN = BEARER_TYPE + SymbolConstants.SPACE;
    String BASIC_TYPE = "Basic";
    String BASIC_TOKEN = BASIC_TYPE + SymbolConstants.SPACE;
    String AUTHORITIES = "authorities";
    String AVATAR = "avatar";
    String EMPLOYEE_ID = "employeeId";
    String LICENSE = "license";
    String OPEN_ID = "openid";
    String PRINCIPAL = "principal";
    String ROLES = "roles";
    String SOURCE = "source";
    String USERNAME = "username";
}
