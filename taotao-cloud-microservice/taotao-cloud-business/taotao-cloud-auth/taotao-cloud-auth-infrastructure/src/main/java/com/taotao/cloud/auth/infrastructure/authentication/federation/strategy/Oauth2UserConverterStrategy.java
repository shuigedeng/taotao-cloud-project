package com.taotao.cloud.auth.infrastructure.authentication.federation.strategy;

import com.taotao.cloud.auth.infrastructure.authentication.federation.Oauth2ThirdAccount;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * oauth2 三方登录获取到的用户信息转换策略接口
 */
public interface Oauth2UserConverterStrategy {

	/**
	 * 将oauth2登录的认证信息转为 {@link Oauth2ThirdAccount}
	 *
	 * @param oAuth2User oauth2登录获取的用户信息
	 * @return 项目中的用户信息
	 */
	Oauth2ThirdAccount convert(OAuth2User oAuth2User);

}
