package com.taotao.cloud.oauth2.api.tmp.social;

import com.taotao.cloud.oauth2.api.tmp.social.user.CustomOAuth2User;
import com.taotao.cloud.oauth2.api.tmp.social.user.UserDetailOAuthUser;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserDetailTypeOAuthUserService extends CustomUserTypesOAuth2UserService {

	private final SocialDetailsService socialDetailsService;

	public UserDetailTypeOAuthUserService(SocialDetailsService socialDetailsService,
		Map<String, Class<? extends OAuth2User>> customUserTypes) {
		super(customUserTypes);
		this.socialDetailsService = socialDetailsService;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User) super.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// 如果当前已处于登录状态,绑定当前账号社交账号
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = null;
		if (authentication != null && !"anonymousUser".equals(authentication.getName())) {
			Object principal = authentication.getPrincipal();
			// 当前登录是密码登录时候
			if (principal instanceof UserDetails) {
				userDetails = (UserDetails) principal;
			}
			// 当前登录为第三方登录
			if (principal instanceof UserDetailOAuthUser) {
				userDetails = ((UserDetailOAuthUser) principal).getUserDetails();
			}
			return new UserDetailOAuthUser(userDetails, oAuth2User);
		}

		// 未登录, 查询用户信息
		userDetails = socialDetailsService.loadUserBySocial(registrationId, oAuth2User.getName());

		if (userDetails == null) {
			throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED,
				"当前社区账号未绑定平台账号", null));
		}

		return new UserDetailOAuthUser(userDetails, oAuth2User);
	}
}
