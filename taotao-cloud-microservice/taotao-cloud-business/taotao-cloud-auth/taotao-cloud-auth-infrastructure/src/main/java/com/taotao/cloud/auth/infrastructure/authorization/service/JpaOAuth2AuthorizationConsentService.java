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

package com.taotao.cloud.auth.infrastructure.authorization.service;

import com.taotao.cloud.auth.infrastructure.persistent.authorization.converter.OAuth2ToTtcAuthorizationConsentConverter;
import com.taotao.cloud.auth.infrastructure.persistent.authorization.converter.TtcToOAuth2AuthorizationConsentConverter;
import com.taotao.cloud.auth.infrastructure.persistent.authorization.po.TtcAuthorizationConsent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>基于 JPA 的 OAuth2 认证服务 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:10:35
 */
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(
		JpaOAuth2AuthorizationConsentService.class);

	/**
	 * 希罗多德授权同意服务
	 */
	private final TtcAuthorizationConsentService ttcAuthorizationConsentService;
	/**
	 * 希罗多德到oauth2转换器
	 */
	private final Converter<TtcAuthorizationConsent, OAuth2AuthorizationConsent> ttcToOAuth2Converter;
	/**
	 * oauth2到ttc转换器
	 */
	private final Converter<OAuth2AuthorizationConsent, TtcAuthorizationConsent> oauth2ToTtcConverter;

	/**
	 * jpa oauth2授权同意服务
	 *
	 * @param ttcAuthorizationConsentService 希罗多德授权同意服务
	 * @param registeredClientRepository     注册客户端存储库
	 * @return
	 * @since 2023-07-10 17:10:36
	 */
	public JpaOAuth2AuthorizationConsentService(
		TtcAuthorizationConsentService ttcAuthorizationConsentService,
		RegisteredClientRepository registeredClientRepository) {
		this.ttcAuthorizationConsentService = ttcAuthorizationConsentService;
		this.ttcToOAuth2Converter =
			new TtcToOAuth2AuthorizationConsentConverter(registeredClientRepository);
		this.oauth2ToTtcConverter = new OAuth2ToTtcAuthorizationConsentConverter();
	}

	/**
	 * 保存
	 *
	 * @param authorizationConsent 授权同意书
	 * @since 2023-07-10 17:10:36
	 */
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		log.info("Jpa OAuth2 Authorization Consent Service save entity.");
		this.ttcAuthorizationConsentService.save(toEntity(authorizationConsent));
	}

	/**
	 * 移除
	 *
	 * @param authorizationConsent 授权同意书
	 * @since 2023-07-10 17:10:36
	 */
	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		log.info("Jpa OAuth2 Authorization Consent Service remove entity.");
		this.ttcAuthorizationConsentService.deleteByRegisteredClientIdAndPrincipalName(
			authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

	/**
	 * 按id查找
	 *
	 * @param registeredClientId 注册客户端id
	 * @param principalName      主体名称
	 * @return {@link OAuth2AuthorizationConsent }
	 * @since 2023-07-10 17:10:36
	 */
	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		log.info("Jpa OAuth2 Authorization Consent Service findById.");
		return this.ttcAuthorizationConsentService
			.findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName)
			.map(this::toObject)
			.orElse(null);
	}

	/**
	 * 反对
	 *
	 * @param authorizationConsent 授权同意书
	 * @return {@link OAuth2AuthorizationConsent }
	 * @since 2023-07-10 17:10:37
	 */
	private OAuth2AuthorizationConsent toObject(TtcAuthorizationConsent authorizationConsent) {
		return ttcToOAuth2Converter.convert(authorizationConsent);
	}

	/**
	 * 对实体
	 *
	 * @param authorizationConsent 授权同意书
	 * @return {@link TtcAuthorizationConsent }
	 * @since 2023-07-10 17:10:37
	 */
	private TtcAuthorizationConsent toEntity(OAuth2AuthorizationConsent authorizationConsent) {
		return oauth2ToTtcConverter.convert(authorizationConsent);
	}
}
