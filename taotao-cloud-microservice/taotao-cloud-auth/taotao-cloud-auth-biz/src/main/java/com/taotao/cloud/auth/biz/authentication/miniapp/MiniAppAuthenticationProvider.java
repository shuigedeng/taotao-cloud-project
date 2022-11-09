package com.taotao.cloud.auth.biz.authentication.miniapp;

import java.util.Collection;
import java.util.Objects;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

public class MiniAppAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

	private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
	private final MiniAppUserDetailsService miniAppUserDetailsService;
	private MiniAppSessionKeyCache miniAppSessionKeyCache;
	private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	public MiniAppAuthenticationProvider(MiniAppUserDetailsService miniAppUserDetailsService,
		MiniAppSessionKeyCache miniAppSessionKeyCache) {
		this.miniAppUserDetailsService = miniAppUserDetailsService;
		this.miniAppSessionKeyCache = miniAppSessionKeyCache;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {
		Assert.isInstanceOf(MiniAppAuthenticationToken.class, authentication,
			() -> messages.getMessage(
				"MiniAppAuthenticationProvider.onlySupports",
				"Only MiniAppAuthenticationToken is supported"));

		MiniAppAuthenticationToken unAuthenticationToken = (MiniAppAuthenticationToken) authentication;
		MiniAppRequest credentials = (MiniAppRequest) unAuthenticationToken.getCredentials();

		String clientId = credentials.getClientId();
		String openId = credentials.getOpenId();

		UserDetails userDetails = miniAppUserDetailsService.loadByOpenId(clientId, openId);
		if (Objects.isNull(userDetails)) {
			userDetails = miniAppUserDetailsService.register(credentials,
				miniAppSessionKeyCache.get(clientId + "::" + openId));
		}
		return createSuccessAuthentication(authentication, userDetails);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return MiniAppAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	/**
	 * 认证成功将非授信凭据转为授信凭据. 封装用户信息 角色信息。
	 *
	 * @param authentication the authentication
	 * @param user           the user
	 * @return the authentication
	 */
	protected Authentication createSuccessAuthentication(Authentication authentication,
		UserDetails user) {

		Collection<? extends GrantedAuthority> authorities = authoritiesMapper.mapAuthorities(
			user.getAuthorities());
		MiniAppAuthenticationToken authenticationToken = new MiniAppAuthenticationToken(user,
			authorities);
		authenticationToken.setDetails(authentication.getPrincipal());

		return authenticationToken;
	}
}
