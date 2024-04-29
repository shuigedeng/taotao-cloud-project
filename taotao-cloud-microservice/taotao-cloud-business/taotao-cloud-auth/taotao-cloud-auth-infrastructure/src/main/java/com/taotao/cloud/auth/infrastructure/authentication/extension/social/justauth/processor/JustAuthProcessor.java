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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.processor;

import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.exception.AccessConfigErrorException;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.exception.IllegalAccessSourceException;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.properties.JustAuthProperties;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.stamp.JustAuthStateStampManager;
import java.util.Map;
import java.util.stream.Collectors;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.request.AuthAliyunRequest;
import me.zhyd.oauth.request.AuthBaiduRequest;
import me.zhyd.oauth.request.AuthCodingRequest;
import me.zhyd.oauth.request.AuthDingTalkRequest;
import me.zhyd.oauth.request.AuthDouyinRequest;
import me.zhyd.oauth.request.AuthElemeRequest;
import me.zhyd.oauth.request.AuthFacebookRequest;
import me.zhyd.oauth.request.AuthFeishuRequest;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthGitlabRequest;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthHuaweiRequest;
import me.zhyd.oauth.request.AuthJdRequest;
import me.zhyd.oauth.request.AuthKujialeRequest;
import me.zhyd.oauth.request.AuthLinkedinRequest;
import me.zhyd.oauth.request.AuthMeituanRequest;
import me.zhyd.oauth.request.AuthMiRequest;
import me.zhyd.oauth.request.AuthMicrosoftRequest;
import me.zhyd.oauth.request.AuthOschinaRequest;
import me.zhyd.oauth.request.AuthPinterestRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRenrenRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthStackOverflowRequest;
import me.zhyd.oauth.request.AuthTaobaoRequest;
import me.zhyd.oauth.request.AuthTeambitionRequest;
import me.zhyd.oauth.request.AuthToutiaoRequest;
import me.zhyd.oauth.request.AuthTwitterRequest;
import me.zhyd.oauth.request.AuthWeChatEnterpriseQrcodeRequest;
import me.zhyd.oauth.request.AuthWeChatEnterpriseWebRequest;
import me.zhyd.oauth.request.AuthWeChatMpRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import me.zhyd.oauth.request.AuthWeiboRequest;
import me.zhyd.oauth.request.AuthXmlyRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.core.util.EnumUtil;
import org.jetbrains.annotations.NotNull;

/**
 * <p>JustAuth请求的生成器 </p>
 *
 * @since : 2021/5/22 11:23
 */
public class JustAuthProcessor {

	private JustAuthProperties justAuthProperties;
	private JustAuthStateStampManager justAuthStateStampManager;

	public void setJustAuthProperties(JustAuthProperties justAuthProperties) {
		this.justAuthProperties = justAuthProperties;
	}

	public void setJustAuthStateRedisCache(JustAuthStateStampManager justAuthStateStampManager) {
		this.justAuthStateStampManager = justAuthStateStampManager;
	}

	private JustAuthStateStampManager getJustAuthStateRedisCache() {
		return justAuthStateStampManager;
	}

	public AuthRequest getAuthRequest(String source) {
		AuthDefaultSource authDefaultSource = parseAuthDefaultSource(source);
		AuthConfig authConfig = getAuthConfig(authDefaultSource);
		return getAuthRequest(authDefaultSource, authConfig);
	}

	public AuthRequest getAuthRequest(String source, AuthConfig authConfig) {
		AuthDefaultSource authDefaultSource = parseAuthDefaultSource(source);
		return getAuthRequest(authDefaultSource, authConfig);
	}

	/**
	 * 返回带state参数的授权url，授权回调时会带上这个state
	 *
	 * @param source 第三方登录的类别 {@link AuthDefaultSource}
	 * @return 返回授权地址
	 */
	public String getAuthorizeUrl(String source) {
		AuthRequest authRequest = this.getAuthRequest(source);
		return authRequest.authorize(AuthStateUtils.createState());
	}

	public String getAuthorizeUrl(String source, AuthConfig authConfig) {
		AuthRequest authRequest = this.getAuthRequest(source, authConfig);
		return authRequest.authorize(AuthStateUtils.createState());
	}

	public Map<String, String> getAuthorizeUrls() {
		Map<String, AuthConfig> configs = getConfigs();
		return configs.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey, entry -> getAuthorizeUrl(entry.getKey(), entry.getValue())));
	}

	@NotNull
	private Map<String, AuthConfig> getConfigs() {
		Map<String, AuthConfig> configs = justAuthProperties.getConfigs();
		if (MapUtils.isEmpty(configs)) {
			throw new AccessConfigErrorException();
		}
		return configs;
	}

	@NotNull
	private AuthConfig getAuthConfig(AuthDefaultSource authDefaultSource) {
		Map<String, AuthConfig> configs = getConfigs();

		AuthConfig authConfig = configs.get(authDefaultSource.name());
		// 找不到对应关系，直接返回空
		if (ObjectUtils.isEmpty(authConfig)) {
			throw new AccessConfigErrorException();
		}
		return authConfig;
	}

	private static AuthDefaultSource parseAuthDefaultSource(String source) {
		AuthDefaultSource authDefaultSource;

		try {
			authDefaultSource = EnumUtil.fromString(AuthDefaultSource.class, source.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new IllegalAccessSourceException();
		}
		return authDefaultSource;
	}

	private AuthRequest getAuthRequest(AuthDefaultSource authDefaultSource, AuthConfig authConfig) {
		switch (authDefaultSource) {
			case GITHUB:
				return new AuthGithubRequest(authConfig, this.getJustAuthStateRedisCache());
			case WEIBO:
				return new AuthWeiboRequest(authConfig, this.getJustAuthStateRedisCache());
			case GITEE:
				return new AuthGiteeRequest(authConfig, this.getJustAuthStateRedisCache());
			case DINGTALK:
				return new AuthDingTalkRequest(authConfig, this.getJustAuthStateRedisCache());
			case BAIDU:
				return new AuthBaiduRequest(authConfig, this.getJustAuthStateRedisCache());
			case CSDN:
				// return new AuthCsdnRequest(authConfig, this.getJustAuthStateRedisCache());
			case CODING:
				return new AuthCodingRequest(authConfig, this.getJustAuthStateRedisCache());
			case OSCHINA:
				return new AuthOschinaRequest(authConfig, this.getJustAuthStateRedisCache());
			case ALIPAY:
				// return new AuthAlipayRequest(authConfig, this.getJustAuthStateRedisCache());
			case QQ:
				return new AuthQqRequest(authConfig, this.getJustAuthStateRedisCache());
			case WECHAT_MP:
				return new AuthWeChatMpRequest(authConfig, this.getJustAuthStateRedisCache());
			case WECHAT_OPEN:
				return new AuthWeChatOpenRequest(authConfig, this.getJustAuthStateRedisCache());
			case WECHAT_ENTERPRISE:
				return new AuthWeChatEnterpriseQrcodeRequest(authConfig,
					this.getJustAuthStateRedisCache());
			case WECHAT_ENTERPRISE_WEB:
				return new AuthWeChatEnterpriseWebRequest(authConfig,
					this.getJustAuthStateRedisCache());
			case TAOBAO:
				return new AuthTaobaoRequest(authConfig, this.getJustAuthStateRedisCache());
			case GOOGLE:
				return new AuthGoogleRequest(authConfig, this.getJustAuthStateRedisCache());
			case FACEBOOK:
				return new AuthFacebookRequest(authConfig, this.getJustAuthStateRedisCache());
			case DOUYIN:
				return new AuthDouyinRequest(authConfig, this.getJustAuthStateRedisCache());
			case LINKEDIN:
				return new AuthLinkedinRequest(authConfig, this.getJustAuthStateRedisCache());
			case MICROSOFT:
				return new AuthMicrosoftRequest(authConfig, this.getJustAuthStateRedisCache());
			case MI:
				return new AuthMiRequest(authConfig, this.getJustAuthStateRedisCache());
			case TOUTIAO:
				return new AuthToutiaoRequest(authConfig, this.getJustAuthStateRedisCache());
			case TEAMBITION:
				return new AuthTeambitionRequest(authConfig, this.getJustAuthStateRedisCache());
			case RENREN:
				return new AuthRenrenRequest(authConfig, this.getJustAuthStateRedisCache());
			case PINTEREST:
				return new AuthPinterestRequest(authConfig, this.getJustAuthStateRedisCache());
			case STACK_OVERFLOW:
				return new AuthStackOverflowRequest(authConfig, this.getJustAuthStateRedisCache());
			case HUAWEI:
				return new AuthHuaweiRequest(authConfig, this.getJustAuthStateRedisCache());
			case GITLAB:
				return new AuthGitlabRequest(authConfig, this.getJustAuthStateRedisCache());
			case KUJIALE:
				return new AuthKujialeRequest(authConfig, this.getJustAuthStateRedisCache());
			case ELEME:
				return new AuthElemeRequest(authConfig, this.getJustAuthStateRedisCache());
			case MEITUAN:
				return new AuthMeituanRequest(authConfig, this.getJustAuthStateRedisCache());
			case TWITTER:
				return new AuthTwitterRequest(authConfig, this.getJustAuthStateRedisCache());
			case FEISHU:
				return new AuthFeishuRequest(authConfig, this.getJustAuthStateRedisCache());
			case JD:
				return new AuthJdRequest(authConfig, this.getJustAuthStateRedisCache());
			case ALIYUN:
				return new AuthAliyunRequest(authConfig, this.getJustAuthStateRedisCache());
			case XMLY:
				return new AuthXmlyRequest(authConfig, this.getJustAuthStateRedisCache());
			default:
				return null;
		}
	}
}
