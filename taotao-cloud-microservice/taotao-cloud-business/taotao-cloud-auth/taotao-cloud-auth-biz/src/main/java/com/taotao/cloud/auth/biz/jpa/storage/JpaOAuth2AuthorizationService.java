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

package com.taotao.cloud.auth.biz.jpa.storage;

import com.taotao.cloud.auth.biz.jpa.converter.OAuth2ToTtcAuthorizationConverter;
import com.taotao.cloud.auth.biz.jpa.converter.TtcToOAuth2AuthorizationConverter;
import com.taotao.cloud.auth.biz.jpa.entity.TtcAuthorization;
import com.taotao.cloud.auth.biz.jpa.jackson2.OAuth2JacksonProcessor;
import com.taotao.cloud.auth.biz.jpa.service.TtcAuthorizationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * <p>基于 JPA 的 OAuth2 认证服务 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:10:41
 */
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(JpaOAuth2AuthorizationService.class);

    /**
     * 希罗多德授权服务
     */
    private final TtcAuthorizationService ttcAuthorizationService;

    /**
     * 希罗多德到oauth2转换器
     */
    private final Converter<TtcAuthorization, OAuth2Authorization> ttcToOAuth2Converter;

    /**
     * oauth2到希罗多德转换器
     */
    private final Converter<OAuth2Authorization, TtcAuthorization> oauth2ToTtcConverter;

    /**
     * jpa oauth2授权服务
     *
     * @param ttcAuthorizationService 希罗多德授权服务
     * @param registeredClientRepository    注册客户端存储库
     * @return
     * @since 2023-07-10 17:10:42
     */
    public JpaOAuth2AuthorizationService(
            TtcAuthorizationService ttcAuthorizationService,
            RegisteredClientRepository registeredClientRepository) {
        this.ttcAuthorizationService = ttcAuthorizationService;

        OAuth2JacksonProcessor jacksonProcessor = new OAuth2JacksonProcessor();
        this.ttcToOAuth2Converter =
                new TtcToOAuth2AuthorizationConverter(jacksonProcessor, registeredClientRepository);
        this.oauth2ToTtcConverter = new OAuth2ToTtcAuthorizationConverter(jacksonProcessor);
    }

    /**
     * 保存
     *
     * @param authorization 授权
     * @since 2023-07-10 17:10:42
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        OAuth2Authorization existingAuthorization = this.findById(authorization.getId());
        if (existingAuthorization == null) {
            this.ttcAuthorizationService.saveAndFlush(toEntity(authorization));
        } else {
            this.ttcAuthorizationService.updateAndFlush(toEntity(authorization));
        }

        log.info("Jpa OAuth2 Authorization Service save entity.");
    }

    /**
     * 移除
     *
     * @param authorization 授权
     * @since 2023-07-10 17:10:42
     */
    @Transactional
    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.ttcAuthorizationService.deleteById(authorization.getId());
        log.info("Jpa OAuth2 Authorization Service remove entity.");
        // TODO： 后期还是考虑改为异步任务的形式，先临时放在这里。
        this.ttcAuthorizationService.clearHistoryToken();
        log.info("Jpa OAuth2 Authorization Service clear history token.");
    }

    /**
     * 按id查找
     *
     * @param id id
     * @return {@link OAuth2Authorization }
     * @since 2023-07-10 17:10:42
     */
    @Override
    public OAuth2Authorization findById(String id) {
        TtcAuthorization ttcAuthorization = this.ttcAuthorizationService.findById(id);
        if (ObjectUtils.isNotEmpty(ttcAuthorization)) {
            log.info("Jpa OAuth2 Authorization Service findById.");
            return toObject(ttcAuthorization);
        } else {
            return null;
        }
    }

    /**
     * 查找授权计数
     *
     * @param registeredClientId 注册客户端id
     * @param principalName      主体名称
     * @return int
     * @since 2023-07-10 17:10:42
     */
    public int findAuthorizationCount(String registeredClientId, String principalName) {
        int count =
                this.ttcAuthorizationService.findAuthorizationCount(
                        registeredClientId, principalName);
        log.info("Jpa OAuth2 Authorization Service findAuthorizationCount.");
        return count;
    }

    /**
     * 查找可用授权
     *
     * @param registeredClientId 注册客户端id
     * @param principalName      主体名称
     * @return {@link List }<{@link OAuth2Authorization }>
     * @since 2023-07-10 17:10:42
     */
    public List<OAuth2Authorization> findAvailableAuthorizations(
            String registeredClientId, String principalName) {
        List<TtcAuthorization> authorizations =
                this.ttcAuthorizationService.findAvailableAuthorizations(
                        registeredClientId, principalName);
        if (CollectionUtils.isNotEmpty(authorizations)) {
            return authorizations.stream().map(this::toObject).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    /**
     * 按令牌查找
     *
     * @param token     令牌
     * @param tokenType 令牌类型
     * @return {@link OAuth2Authorization }
     * @since 2023-07-10 17:10:43
     */
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        Optional<TtcAuthorization> result;
        if (tokenType == null) {
            result =
                    this.ttcAuthorizationService
                            .findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
                                    token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            result = this.ttcAuthorizationService.findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            result = this.ttcAuthorizationService.findByAuthorizationCode(token);
        } else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
            result = this.ttcAuthorizationService.findByAccessToken(token);
        } else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
            result = this.ttcAuthorizationService.findByRefreshToken(token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            result = this.ttcAuthorizationService.findByOidcIdTokenValue(token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            result = this.ttcAuthorizationService.findByUserCodeValue(token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            result = this.ttcAuthorizationService.findByDeviceCodeValue(token);
        } else {
            result = Optional.empty();
        }

        log.info("Jpa OAuth2 Authorization Service findByToken.");
        return result.map(this::toObject).orElse(null);
    }

    /**
     * 反对
     *
     * @param entity 实体
     * @return {@link OAuth2Authorization }
     * @since 2023-07-10 17:10:43
     */
    private OAuth2Authorization toObject(TtcAuthorization entity) {
        return ttcToOAuth2Converter.convert(entity);
    }

    /**
     * 对实体
     *
     * @param authorization 授权
     * @return {@link TtcAuthorization }
     * @since 2023-07-10 17:10:43
     */
    private TtcAuthorization toEntity(OAuth2Authorization authorization) {
        return oauth2ToTtcConverter.convert(authorization);
    }
}
