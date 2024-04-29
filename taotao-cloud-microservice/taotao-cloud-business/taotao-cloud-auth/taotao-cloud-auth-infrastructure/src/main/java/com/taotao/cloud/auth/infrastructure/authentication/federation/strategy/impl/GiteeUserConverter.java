package com.taotao.cloud.auth.infrastructure.authentication.federation.strategy.impl;

import com.taotao.cloud.auth.infrastructure.authentication.federation.Oauth2ThirdAccount;
import com.taotao.cloud.auth.infrastructure.authentication.federation.strategy.Oauth2UserConverterStrategy;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * 转换通过码云登录的用户信息
 */
@Component(GiteeUserConverter.THIRD_LOGIN_GITEE)
public class GiteeUserConverter implements Oauth2UserConverterStrategy {
	/**
	 * 三方登录类型——Gitee
	 */
	public static final String THIRD_LOGIN_GITEE = "gitee";

	@Override
	public Oauth2ThirdAccount convert(OAuth2User oAuth2User) {
		// 获取三方用户信息
		Map<String, Object> attributes = oAuth2User.getAttributes();
		// 转换至Oauth2ThirdAccount
		Oauth2ThirdAccount thirdAccount = new Oauth2ThirdAccount();
		thirdAccount.setUniqueId(oAuth2User.getName());
		thirdAccount.setThirdUsername(String.valueOf(attributes.get("login")));
		thirdAccount.setType(THIRD_LOGIN_GITEE);
		thirdAccount.setBlog(String.valueOf(attributes.get("blog")));
		// 设置基础用户信息
		thirdAccount.setName(String.valueOf(attributes.get("name")));
		thirdAccount.setAvatarUrl(String.valueOf(attributes.get("avatar_url")));
		return thirdAccount;
	}
}
