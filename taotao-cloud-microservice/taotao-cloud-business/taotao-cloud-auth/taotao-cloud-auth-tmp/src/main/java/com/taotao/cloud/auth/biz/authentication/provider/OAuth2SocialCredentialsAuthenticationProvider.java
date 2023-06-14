/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
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
 * 4.分发源码时候，请注明软件出处
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.provider;

import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.biz.authentication.utils.OAuth2AuthenticationProviderUtils;
import com.taotao.cloud.security.springsecurity.core.constants.BaseConstants;
import com.taotao.cloud.security.springsecurity.core.definition.HerodotusGrantType;
import com.taotao.cloud.security.springsecurity.core.definition.domain.AccessPrincipal;
import com.taotao.cloud.security.springsecurity.core.definition.service.EnhanceUserDetailsService;
import com.taotao.cloud.security.springsecurity.core.exception.SocialCredentialsParameterBindingFailedException;
import org.dromara.hutool.core.bean.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebRequestDataBinder;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

/**
 * <p>Description: 社会化认证 Provider </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/31 14:54
 */
public class OAuth2SocialCredentialsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private static final Logger log = LoggerFactory.getLogger(OAuth2SocialCredentialsAuthenticationProvider.class);

	private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	private final OAuth2AuthorizationService authorizationService;
	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
	private SessionRegistry sessionRegistry;

	public OAuth2SocialCredentialsAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, UserDetailsService userDetailsService, OAuth2AuthenticationProperties complianceProperties) {
		super(authorizationService, userDetailsService, complianceProperties);
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, Map<String, Object> additionalParameters) throws AuthenticationException {

	}

	@Override
	protected UserDetails retrieveUser(Map<String, Object> additionalParameters) throws AuthenticationException {
		String source = (String) additionalParameters.get(BaseConstants.SOURCE);
		AccessPrincipal accessPrincipal = parameterBinder(additionalParameters);

		try {
			EnhanceUserDetailsService enhanceUserDetailsService = getUserDetailsService();
			UserDetails userDetails = enhanceUserDetailsService.loadUserBySocial(source, accessPrincipal);
			if (userDetails == null) {
				throw new InternalAuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
			}
			return userDetails;
		} catch (UsernameNotFoundException ex) {
			log.error("[Herodotus] |- User name can not found for：[{}]", source);
			throw ex;
		} catch (InternalAuthenticationServiceException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		OAuth2SocialCredentialsAuthenticationToken socialCredentialsAuthentication =
			(OAuth2SocialCredentialsAuthenticationToken) authentication;

		OAuth2ClientAuthenticationToken clientPrincipal =
			OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(socialCredentialsAuthentication);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		if (!registeredClient.getAuthorizationGrantTypes().contains(HerodotusGrantType.SOCIAL)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}

		Authentication principal = getUsernamePasswordAuthentication(socialCredentialsAuthentication.getAdditionalParameters(), registeredClient.getId());

		// Default to configured scopes
		Set<String> authorizedScopes = validateScopes(socialCredentialsAuthentication.getScopes(), registeredClient);

		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName(principal.getName())
			.authorizationGrantType(HerodotusGrantType.SOCIAL)
			.authorizedScopes(authorizedScopes)
			.attribute(Principal.class.getName(), principal);

		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
			.registeredClient(registeredClient)
			.principal(principal)
			.authorizationServerContext(AuthorizationServerContextHolder.getContext())
			.authorizedScopes(authorizedScopes)
			.tokenType(OAuth2TokenType.ACCESS_TOKEN)
			.authorizationGrantType(HerodotusGrantType.SOCIAL)
			.authorizationGrant(socialCredentialsAuthentication);

		// ----- Access token -----
		OAuth2AccessToken accessToken = createOAuth2AccessToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI);

		// ----- Refresh token -----
		OAuth2RefreshToken refreshToken = creatOAuth2RefreshToken(tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI, clientPrincipal, registeredClient);

		// ----- ID token -----
		OidcIdToken idToken = createOidcIdToken(principal, sessionRegistry, tokenContextBuilder, authorizationBuilder, this.tokenGenerator, ERROR_URI, socialCredentialsAuthentication.getScopes());

		OAuth2Authorization authorization = authorizationBuilder.build();

		this.authorizationService.save(authorization);

		log.info("[Herodotus] |- Social Credential returning OAuth2AccessTokenAuthenticationToken.");

		Map<String, Object> additionalParameters = idTokenAdditionalParameters(idToken);

		OAuth2AccessTokenAuthenticationToken accessTokenAuthenticationToken = new OAuth2AccessTokenAuthenticationToken(
			registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
		return createOAuth2AccessTokenAuthenticationToken(principal, accessTokenAuthenticationToken);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = OAuth2SocialCredentialsAuthenticationToken.class.isAssignableFrom(authentication);
		log.trace("[Herodotus] |- Resource Owner Password Authentication is supports! [{}]", supports);
		return supports;
	}

	/**
	 * Sets the {@link SessionRegistry} used to track OpenID Connect sessions.
	 *
	 * @param sessionRegistry the {@link SessionRegistry} used to track OpenID Connect sessions
	 * @since 1.1.0
	 */
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		Assert.notNull(sessionRegistry, "sessionRegistry cannot be null");
		this.sessionRegistry = sessionRegistry;
	}

	private AccessPrincipal parameterBinder(Map<String, Object> parameters) throws SocialCredentialsParameterBindingFailedException {
		AccessPrincipal accessPrincipal = new AccessPrincipal();

		MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(parameters);

		WebRequestDataBinder webRequestDataBinder = new WebRequestDataBinder(accessPrincipal);
		webRequestDataBinder.bind(mutablePropertyValues);
		if (BeanUtil.isNotEmpty(accessPrincipal)) {
			return accessPrincipal;
		}

		throw new SocialCredentialsParameterBindingFailedException("Internet authentication parameter bindng is not correct!");
	}


}
