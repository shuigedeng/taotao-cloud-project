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

import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: 平台端点属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/6 12:00
 */
@ConfigurationProperties(prefix = RestConstants.PROPERTY_PREFIX_ENDPOINT)
public class EndpointProperties {

    private static final Logger log = LoggerFactory.getLogger(EndpointProperties.class);

    /**
     * 认证中心服务名称
     */
    private String uaaServiceName;
    /**
     * 用户中心服务名称
     */
    private String upmsServiceName;

    /**
     * 统一网关服务地址。可以是IP+端口，可以是域名
     */
    private String gatewayServiceUri;

    /**
     * 统一认证中心服务地址
     */
    private String uaaServiceUri;
    /**
     * 统一权限管理服务地址
     */
    private String upmsServiceUri;
    /**
     * OAuth2 Authorization Code 模式认证端点，/oauth2/authorize uri 地址，可修改为自定义地址
     */
    private String authorizationUri;
    /**
     * OAuth2 Authorization Code 模式认证端点，/oauth2/authorize端点地址，可修改为自定义地址
     */
    private String authorizationEndpoint = BaseConstants.DEFAULT_AUTHORIZATION_ENDPOINT;
    /**
     * OAuth2 /oauth2/token 申请 Token uri 地址，可修改为自定义地址
     */
    private String accessTokenUri;
    /**
     * OAuth2 /oauth2/token 申请 Token 端点地址，可修改为自定义地址
     */
    private String accessTokenEndpoint = BaseConstants.DEFAULT_TOKEN_ENDPOINT;
    /**
     * OAuth2 /oauth2/jwks uri 地址，可修改为自定义地址
     */
    private String jwkSetUri;
    /**
     * OAuth2 /oauth2/jwks 端点地址，可修改为自定义地址
     */
    private String jwkSetEndpoint = BaseConstants.DEFAULT_JWK_SET_ENDPOINT;
    /**
     * OAuth2 /oauth2/revoke 撤销 Token uri 地址，可修改为自定义地址
     */
    private String tokenRevocationUri;
    /**
     * OAuth2 /oauth2/revoke 撤销 Token 端点地址，可修改为自定义地址
     */
    private String tokenRevocationEndpoint = BaseConstants.DEFAULT_TOKEN_REVOCATION_ENDPOINT;
    /**
     * OAuth2 /oauth2/introspect 查看 Token uri地址，可修改为自定义地址
     */
    private String tokenIntrospectionUri;
    /**
     * OAuth2 /oauth2/introspect 查看 Token 端点地址，可修改为自定义地址
     */
    private String tokenIntrospectionEndpoint = BaseConstants.DEFAULT_TOKEN_INTROSPECTION_ENDPOINT;
    /**
     * OAuth2 /oauth2/device_authorization 设备授权认证 uri地址，可修改为自定义地址
     */
    private String deviceAuthorizationUri;
    /**
     * OAuth2 /oauth2/device_authorization 设备授权认证端点地址，可修改为自定义地址
     */
    private String deviceAuthorizationEndpoint = BaseConstants.DEFAULT_DEVICE_AUTHORIZATION_ENDPOINT;
    /**
     * OAuth2 /oauth2/device_verification 设备授权校验 uri地址，可修改为自定义地址
     */
    private String deviceVerificationUri;
    /**
     * OAuth2 /oauth2/device_verification 设备授权校验端点地址，可修改为自定义地址
     */
    private String deviceVerificationEndpoint = BaseConstants.DEFAULT_DEVICE_VERIFICATION_ENDPOINT;
    /**
     * OAuth2 OIDC /connect/register uri 地址，可修改为自定义地址
     */
    private String oidcClientRegistrationUri;
    /**
     * OAuth2 OIDC /connect/register 端点地址，可修改为自定义地址
     */
    private String oidcClientRegistrationEndpoint = BaseConstants.DEFAULT_OIDC_CLIENT_REGISTRATION_ENDPOINT;
    /**
     * OAuth2 OIDC /connect/logout uri 地址，可修改为自定义地址
     */
    private String oidcLogoutUri;
    /**
     * OAuth2 OIDC /connect/logout 端点地址，可修改为自定义地址
     */
    private String oidcLogoutEndpoint = BaseConstants.DEFAULT_OIDC_LOGOUT_ENDPOINT;
    /**
     * OAuth2 OIDC /userinfo uri 地址，可修改为自定义地址
     */
    private String oidcUserInfoUri;
    /**
     * OAuth2 OIDC /userinfo 端点地址，可修改为自定义地址
     */
    private String oidcUserInfoEndpoint = BaseConstants.DEFAULT_OIDC_USER_INFO_ENDPOINT;
    /**
     * Spring Authorization Server Issuer Url
     */
    private String issuerUri;

    public String getUaaServiceName() {
        return uaaServiceName;
    }

    public void setUaaServiceName(String uaaServiceName) {
        this.uaaServiceName = uaaServiceName;
    }

    public String getUpmsServiceName() {
        return upmsServiceName;
    }

    public void setUpmsServiceName(String upmsServiceName) {
        this.upmsServiceName = upmsServiceName;
    }

    public String getGatewayServiceUri() {
        return gatewayServiceUri;
    }

    public void setGatewayServiceUri(String gatewayServiceUri) {
        this.gatewayServiceUri = gatewayServiceUri;
    }

    public String getUaaServiceUri() {
        if (StringUtils.isNotBlank(uaaServiceUri)) {
            return uaaServiceUri;
        } else {
            if (StringUtils.isBlank(uaaServiceName)) {
                log.error("[Herodotus] |- Property [Uaa Service Name] is not set or property format is incorrect!");
                throw new PropertyValueIsNotSetException();
            } else {
                return ConvertUtils.wellFormed(getGatewayServiceUri()) + uaaServiceName;
            }
        }
    }

    public void setUaaServiceUri(String uaaServiceUri) {
        this.uaaServiceUri = uaaServiceUri;
    }

    private String getDefaultEndpoint(String endpoint, String pathAuthorizationEndpoint) {
        if (StringUtils.isNotBlank(endpoint)) {
            return endpoint;
        } else {
            if (StringUtils.isNotBlank(pathAuthorizationEndpoint)) {
                return getUaaServiceUri() + pathAuthorizationEndpoint;
            } else {
                return getUaaServiceUri();
            }
        }
    }

    public String getUpmsServiceUri() {
        if (StringUtils.isNotBlank(upmsServiceUri)) {
            return upmsServiceUri;
        } else {
            if (StringUtils.isBlank(upmsServiceName)) {
                log.error("[Herodotus] |- Property [Upms Service Name] is not set or property format is incorrect!");
                throw new PropertyValueIsNotSetException();
            } else {
                return ConvertUtils.wellFormed(getGatewayServiceUri()) + upmsServiceName;
            }
        }
    }

    public void setUpmsServiceUri(String upmsServiceUri) {
        this.upmsServiceUri = upmsServiceUri;
    }

    public String getAuthorizationUri() {
        return getDefaultEndpoint(authorizationUri, getAuthorizationEndpoint());
    }


    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getAccessTokenUri() {
        return getDefaultEndpoint(accessTokenUri, getAccessTokenEndpoint());
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getJwkSetUri() {
        return getDefaultEndpoint(jwkSetUri, getJwkSetEndpoint());
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    public String getTokenRevocationUri() {
        return getDefaultEndpoint(tokenRevocationUri, getTokenRevocationEndpoint());
    }

    public void setTokenRevocationUri(String tokenRevocationUri) {
        this.tokenRevocationUri = tokenRevocationUri;
    }

    public String getTokenIntrospectionUri() {
        return getDefaultEndpoint(tokenIntrospectionUri, getTokenIntrospectionEndpoint());
    }

    public void setTokenIntrospectionUri(String tokenIntrospectionUri) {
        this.tokenIntrospectionUri = tokenIntrospectionUri;
    }

    public String getDeviceAuthorizationUri() {
        return getDefaultEndpoint(deviceAuthorizationUri, getDeviceAuthorizationEndpoint());
    }

    public void setDeviceAuthorizationUri(String deviceAuthorizationUri) {
        this.deviceAuthorizationUri = deviceAuthorizationUri;
    }

    public String getDeviceVerificationUri() {
        return getDefaultEndpoint(deviceVerificationUri, getDeviceVerificationEndpoint());
    }

    public void setDeviceVerificationUri(String deviceVerificationUri) {
        this.deviceVerificationUri = deviceVerificationUri;
    }

    public String getOidcClientRegistrationUri() {
        return getDefaultEndpoint(oidcClientRegistrationUri, getOidcClientRegistrationEndpoint());
    }

    public void setOidcClientRegistrationUri(String oidcClientRegistrationUri) {
        this.oidcClientRegistrationUri = oidcClientRegistrationUri;
    }

    public String getOidcLogoutUri() {
        return getDefaultEndpoint(oidcLogoutUri, getOidcLogoutEndpoint());
    }

    public void setOidcLogoutUri(String oidcLogoutUri) {
        this.oidcLogoutUri = oidcLogoutUri;
    }

    public String getOidcUserInfoUri() {
        return getDefaultEndpoint(oidcUserInfoUri, getOidcUserInfoEndpoint());
    }

    public void setOidcUserInfoUri(String oidcUserInfoUri) {
        this.oidcUserInfoUri = oidcUserInfoUri;
    }

    public String getIssuerUri() {
        return this.issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getAccessTokenEndpoint() {
        return accessTokenEndpoint;
    }

    public void setAccessTokenEndpoint(String accessTokenEndpoint) {
        this.accessTokenEndpoint = accessTokenEndpoint;
    }

    public String getJwkSetEndpoint() {
        return jwkSetEndpoint;
    }

    public void setJwkSetEndpoint(String jwkSetEndpoint) {
        this.jwkSetEndpoint = jwkSetEndpoint;
    }

    public String getTokenRevocationEndpoint() {
        return tokenRevocationEndpoint;
    }

    public void setTokenRevocationEndpoint(String tokenRevocationEndpoint) {
        this.tokenRevocationEndpoint = tokenRevocationEndpoint;
    }

    public String getTokenIntrospectionEndpoint() {
        return tokenIntrospectionEndpoint;
    }

    public void setTokenIntrospectionEndpoint(String tokenIntrospectionEndpoint) {
        this.tokenIntrospectionEndpoint = tokenIntrospectionEndpoint;
    }

    public String getDeviceAuthorizationEndpoint() {
        return deviceAuthorizationEndpoint;
    }

    public void setDeviceAuthorizationEndpoint(String deviceAuthorizationEndpoint) {
        this.deviceAuthorizationEndpoint = deviceAuthorizationEndpoint;
    }

    public String getDeviceVerificationEndpoint() {
        return deviceVerificationEndpoint;
    }

    public void setDeviceVerificationEndpoint(String deviceVerificationEndpoint) {
        this.deviceVerificationEndpoint = deviceVerificationEndpoint;
    }

    public String getOidcClientRegistrationEndpoint() {
        return oidcClientRegistrationEndpoint;
    }

    public void setOidcClientRegistrationEndpoint(String oidcClientRegistrationEndpoint) {
        this.oidcClientRegistrationEndpoint = oidcClientRegistrationEndpoint;
    }

    public String getOidcUserInfoEndpoint() {
        return oidcUserInfoEndpoint;
    }

    public void setOidcUserInfoEndpoint(String oidcUserInfoEndpoint) {
        this.oidcUserInfoEndpoint = oidcUserInfoEndpoint;
    }

    public String getOidcLogoutEndpoint() {
        return oidcLogoutEndpoint;
    }

    public void setOidcLogoutEndpoint(String oidcLogoutEndpoint) {
        this.oidcLogoutEndpoint = oidcLogoutEndpoint;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("uaaServiceName", uaaServiceName)
                .add("upmsServiceName", upmsServiceName)
                .add("gatewayServiceUri", gatewayServiceUri)
                .add("uaaServiceUri", uaaServiceUri)
                .add("upmsServiceUri", upmsServiceUri)
                .add("authorizationUri", authorizationUri)
                .add("authorizationEndpoint", authorizationEndpoint)
                .add("accessTokenUri", accessTokenUri)
                .add("accessTokenEndpoint", accessTokenEndpoint)
                .add("jwkSetUri", jwkSetUri)
                .add("jwkSetEndpoint", jwkSetEndpoint)
                .add("tokenRevocationUri", tokenRevocationUri)
                .add("tokenRevocationEndpoint", tokenRevocationEndpoint)
                .add("tokenIntrospectionUri", tokenIntrospectionUri)
                .add("tokenIntrospectionEndpoint", tokenIntrospectionEndpoint)
                .add("deviceAuthorizationUri", deviceAuthorizationUri)
                .add("deviceAuthorizationEndpoint", deviceAuthorizationEndpoint)
                .add("deviceVerificationUri", deviceVerificationUri)
                .add("deviceVerificationEndpoint", deviceVerificationEndpoint)
                .add("oidcClientRegistrationUri", oidcClientRegistrationUri)
                .add("oidcClientRegistrationEndpoint", oidcClientRegistrationEndpoint)
                .add("oidcLogoutUri", oidcLogoutUri)
                .add("oidcLogoutEndpoint", oidcLogoutEndpoint)
                .add("oidcUserInfoUri", oidcUserInfoUri)
                .add("oidcUserInfoEndpoint", oidcUserInfoEndpoint)
                .add("issuerUri", issuerUri)
                .toString();
    }
}
