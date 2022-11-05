package com.taotao.cloud.auth.biz.authentication.qrcocde;

import com.taotao.cloud.auth.biz.authentication.qrcocde.service.QrcodeService;
import com.taotao.cloud.auth.biz.authentication.qrcocde.service.QrcodeUserDetailsService;
import java.util.Collection;
import org.springframework.beans.factory.InitializingBean;
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

/**
 * 用户+密码登录
 */
public class QrcodeAuthenticationProvider implements AuthenticationProvider, InitializingBean,
	MessageSourceAware {

	private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
	private final QrcodeUserDetailsService qrcodeUserDetailsService;
	private final QrcodeService qrcodeService;
	private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	public QrcodeAuthenticationProvider(QrcodeService qrcodeService,
		QrcodeUserDetailsService qrcodeUserDetailsService) {
		this.qrcodeService = qrcodeService;
		this.qrcodeUserDetailsService = qrcodeUserDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {
		Assert.isInstanceOf(QrcodeAuthenticationToken.class, authentication,
			() -> messages.getMessage(
				"AccountVerificationAuthenticationProvider.onlySupports",
				"Only AccountVerificationAuthenticationProvider is supported"));

		QrcodeAuthenticationToken unAuthenticationToken = (QrcodeAuthenticationToken) authentication;

		String username = unAuthenticationToken.getName();
		String passowrd = (String) unAuthenticationToken.getCredentials();

		qrcodeService.verifyQrcode("qrocde");

		// 验证码校验
		UserDetails userDetails = qrcodeUserDetailsService.loadUserByPhone(username);
		// 校验密码
		//TODO 此处省略对UserDetails 的可用性 是否过期  是否锁定 是否失效的检验  建议根据实际情况添加  或者在 UserDetailsService 的实现中处理
		return createSuccessAuthentication(authentication, userDetails);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return QrcodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(qrcodeUserDetailsService, "qrcodeUserDetailsService must not be null");
		Assert.notNull(qrcodeService, "qrcodeService must not be null");
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
		QrcodeAuthenticationToken authenticationToken = new QrcodeAuthenticationToken(user, null,
			authorities);
		authenticationToken.setDetails(authentication.getDetails());

		return authenticationToken;
	}

}
