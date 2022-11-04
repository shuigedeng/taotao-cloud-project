package com.taotao.cloud.auth.biz.authentication.accountVerification;

import com.taotao.cloud.auth.biz.authentication.accountVerification.service.AccountVerificationService;
import com.taotao.cloud.auth.biz.authentication.accountVerification.service.AccountVerificationUserDetailsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 手机号码+短信登录
 */
public class AccountVerificationAuthenticationProvider implements AuthenticationProvider, InitializingBean,
	MessageSourceAware {

	private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
	private final AccountVerificationUserDetailsService accountVerificationUserDetailsService;
	private final AccountVerificationService accountVerificationService;
	private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	public AccountVerificationAuthenticationProvider(AccountVerificationUserDetailsService accountVerificationUserDetailsService,
													 AccountVerificationService accountVerificationService) {
		this.accountVerificationUserDetailsService = accountVerificationUserDetailsService;
		this.accountVerificationService = accountVerificationService;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {
		Assert.isInstanceOf(AccountVerificationAuthenticationToken.class, authentication,
			() -> messages.getMessage(
				"AccountVerificationAuthenticationProvider.onlySupports",
				"Only AccountVerificationAuthenticationProvider is supported"));

		AccountVerificationAuthenticationToken unAuthenticationToken = (AccountVerificationAuthenticationToken) authentication;

		String username = unAuthenticationToken.getName();
		String passowrd = (String) unAuthenticationToken.getCredentials();
		String verificationCode = unAuthenticationToken.getVerificationCode();

		// 验证码校验
		if (accountVerificationService.verifyCaptcha(verificationCode)) {
			UserDetails userDetails = accountVerificationUserDetailsService.loadUserByPhone(username);
			// 校验密码
			//TODO 此处省略对UserDetails 的可用性 是否过期  是否锁定 是否失效的检验  建议根据实际情况添加  或者在 UserDetailsService 的实现中处理
			return createSuccessAuthentication(authentication, userDetails);
		} else {
			throw new BadCredentialsException("verificationCode is not matched");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AccountVerificationAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(accountVerificationUserDetailsService, "captchaUserDetailsService must not be null");
		Assert.notNull(accountVerificationService, "captchaService must not be null");
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
		AccountVerificationAuthenticationToken authenticationToken = new AccountVerificationAuthenticationToken(user, null, null,
			authorities);
		authenticationToken.setDetails(authentication.getDetails());

		return authenticationToken;
	}

}
