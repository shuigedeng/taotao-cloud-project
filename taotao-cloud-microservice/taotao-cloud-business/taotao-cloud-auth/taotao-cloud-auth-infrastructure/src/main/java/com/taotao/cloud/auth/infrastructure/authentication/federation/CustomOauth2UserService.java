package com.taotao.cloud.auth.infrastructure.authentication.federation;

import com.taotao.cloud.auth.infrastructure.authentication.federation.strategy.context.Oauth2UserConverterContext;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * 自定义三方oauth2登录获取用户信息服务
 */
//@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

	private final IOauth2ThirdAccountService thirdAccountService;

	private final Oauth2UserConverterContext userConverterContext;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		// 转为项目中的三方用户信息
		Oauth2ThirdAccount oauth2ThirdAccount = userConverterContext.convert(userRequest, oAuth2User);
		// 检查用户信息
		thirdAccountService.checkAndSaveUser(oauth2ThirdAccount);
		// 将loginType设置至attributes中
		LinkedHashMap<String, Object> attributes = new LinkedHashMap<>(oAuth2User.getAttributes());
		// 将RegistrationId当做登录类型
		attributes.put("loginType", userRequest.getClientRegistration().getRegistrationId());
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
			.getUserNameAttributeName();
		return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, userNameAttributeName);
	}
}
