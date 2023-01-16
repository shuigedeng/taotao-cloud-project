/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.demo.server.dto;

import cn.herodotus.engine.assistant.core.json.jackson2.deserializer.ArrayOrStringDeserializer;
import cn.herodotus.engine.oauth2.core.enums.ApplicationType;
import cn.herodotus.engine.oauth2.core.enums.Signature;
import cn.herodotus.engine.oauth2.core.enums.TokenFormat;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Scope;
import cn.herodotus.engine.rest.core.definition.dto.BaseSysDto;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

/**
 * <p>Description: OAuth2 Application Dto </p>
 * <p>
 * 方便数据的转换
 *
 * @author : gengwei.zheng
 * @date : 2022/3/18 14:56
 */
public class OAuth2ApplicationDto extends BaseSysDto {

    @Schema(name = "应用ID")
    private String applicationId;

    @Schema(name = "应用名称", required = true)
    private String applicationName;

    @Schema(name = "应用简称", title = "应用的简称、别名、缩写等信息")
    private String abbreviation;

    @Schema(name = "Logo", title = "Logo存储信息，可以是URL或者路径等")
    private String logo;

    @Schema(name = "主页信息", title = "应用相关的主页信息方便查询")
    private String homepage;

    @Schema(name = "应用类型", title = "用于区分不同类型的应用")
    private ApplicationType applicationType = ApplicationType.WEB;

    @Schema(name = "客户端Id", title = "默认为系统自动生成")
    private String clientId = IdUtil.fastSimpleUUID();

    @Schema(name = "客户端秘钥", title = "这里存储的客户端秘钥是明文，方便使用。默认为系统自动生成")
    private String clientSecret = IdUtil.fastSimpleUUID();

    @Schema(name = "客户端秘钥过期时间", title = "客户端秘钥过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "GMT+8", shape = JsonFormat.Shape.STRING)
    private LocalDateTime clientSecretExpiresAt;

    @Schema(name = "回调地址", title = "支持多个值，以逗号分隔", required = true)
    private String redirectUris;

    @Schema(name = "认证模式", title = "支持多个值，以逗号分隔", required = true)
    @JsonDeserialize(using = ArrayOrStringDeserializer.class)
    private Set<String> authorizationGrantTypes = Collections.emptySet();

    @Schema(name = "客户端认证模式", title = "支持多个值，以逗号分隔", required = true)
    @JsonDeserialize(using = ArrayOrStringDeserializer.class)
    private Set<String> clientAuthenticationMethods = Collections.emptySet();

    @Schema(name = "需要证明Key", title = "如果客户端在执行授权码授予流时需要提供验证密钥质询和验证器, 默认False")
    private Boolean requireProofKey = Boolean.FALSE;

    @Schema(name = "需要认证确认", title = "如果客户端在执行授权码授予流时需要提供验证密钥质询和验证器, 默认False")
    private Boolean requireAuthorizationConsent = Boolean.TRUE;

    @Schema(name = "客户端JSON Web密钥集的URL", title = "客户端JSON Web密钥集的URL")
    private String jwkSetUrl;

    @Schema(name = "JWT 签名算法", title = "仅在 clientAuthenticationMethods 为 private_key_jwt 和 client_secret_jwt 方法下使用")
    private Signature authenticationSigningAlgorithm;

    @Schema(name = "Access Token", title = "OAuth 2.0令牌的标准数据格式")
    private TokenFormat accessTokenFormat = TokenFormat.SELF_CONTAINED;

    @Schema(name = "accessToken 有效时间", title = "默认5分钟，使用 Duration 时间格式")
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration accessTokenValidity = Duration.ofSeconds(5);

    @Schema(name = "是否重用 Refresh Token", title = "默认值 True")
    private Boolean reuseRefreshTokens = Boolean.TRUE;

    @Schema(name = "refreshToken 有效时间", title = "默认60分钟，使用 Duration 时间格式")
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration refreshTokenValidity = Duration.ofHours(1);

    @Schema(name = "IdToken 签名算法", title = "JWT 算法用于签名 ID Token， 默认值 RS256")
    private Signature idTokenSignatureAlgorithm = Signature.RS256;

    @Schema(name = "应用对应Scope", title = "传递应用对应Scope ID")
    private Set<OAuth2Scope> scopes = Collections.emptySet();

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public Set<String> getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(Set<String> authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public Set<String> getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(Set<String> clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public Boolean getRequireProofKey() {
        return requireProofKey;
    }

    public void setRequireProofKey(Boolean requireProofKey) {
        this.requireProofKey = requireProofKey;
    }

    public Boolean getRequireAuthorizationConsent() {
        return requireAuthorizationConsent;
    }

    public void setRequireAuthorizationConsent(Boolean requireAuthorizationConsent) {
        this.requireAuthorizationConsent = requireAuthorizationConsent;
    }

    public String getJwkSetUrl() {
        return jwkSetUrl;
    }

    public void setJwkSetUrl(String jwkSetUrl) {
        this.jwkSetUrl = jwkSetUrl;
    }

    public Duration getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Duration accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Boolean getReuseRefreshTokens() {
        return reuseRefreshTokens;
    }

    public void setReuseRefreshTokens(Boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }

    public Duration getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Duration refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public Signature getIdTokenSignatureAlgorithm() {
        return idTokenSignatureAlgorithm;
    }

    public void setIdTokenSignatureAlgorithm(Signature idTokenSignatureAlgorithm) {
        this.idTokenSignatureAlgorithm = idTokenSignatureAlgorithm;
    }

    public Set<OAuth2Scope> getScopes() {
        return scopes;
    }

    public void setScopes(Set<OAuth2Scope> scopes) {
        this.scopes = scopes;
    }

    public LocalDateTime getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(LocalDateTime clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public Signature getAuthenticationSigningAlgorithm() {
        return authenticationSigningAlgorithm;
    }

    public void setAuthenticationSigningAlgorithm(Signature authenticationSigningAlgorithm) {
        this.authenticationSigningAlgorithm = authenticationSigningAlgorithm;
    }

    public TokenFormat getAccessTokenFormat() {
        return accessTokenFormat;
    }

    public void setAccessTokenFormat(TokenFormat accessTokenFormat) {
        this.accessTokenFormat = accessTokenFormat;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("applicationId", applicationId)
                .add("applicationName", applicationName)
                .add("abbreviation", abbreviation)
                .add("logo", logo)
                .add("homepage", homepage)
                .add("applicationType", applicationType)
                .add("clientId", clientId)
                .add("clientSecret", clientSecret)
                .add("clientSecretExpiresAt", clientSecretExpiresAt)
                .add("redirectUris", redirectUris)
                .add("authorizationGrantTypes", authorizationGrantTypes)
                .add("clientAuthenticationMethods", clientAuthenticationMethods)
                .add("requireProofKey", requireProofKey)
                .add("requireAuthorizationConsent", requireAuthorizationConsent)
                .add("jwkSetUrl", jwkSetUrl)
                .add("authenticationSigningAlgorithm", authenticationSigningAlgorithm)
                .add("accessTokenFormat", accessTokenFormat)
                .add("accessTokenValidity", accessTokenValidity)
                .add("reuseRefreshTokens", reuseRefreshTokens)
                .add("refreshTokenValidity", refreshTokenValidity)
                .add("idTokenSignatureAlgorithm", idTokenSignatureAlgorithm)
                .toString();
    }
}
