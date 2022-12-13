/*
 * Copyright (c) 2019-2029, xkcoding & Yangkai.Shen & 沈扬凯 (237497819@qq.com & xkcoding.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.taotao.cloud.security.justauth.factory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.security.justauth.properties.ExtendProperties;
import com.taotao.cloud.security.justauth.properties.JustAuthProperties;
import com.xkcoding.http.config.HttpConfig;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.AuthAlipayRequest;
import me.zhyd.oauth.request.AuthAliyunRequest;
import me.zhyd.oauth.request.AuthAmazonRequest;
import me.zhyd.oauth.request.AuthBaiduRequest;
import me.zhyd.oauth.request.AuthCodingRequest;
import me.zhyd.oauth.request.AuthCsdnRequest;
import me.zhyd.oauth.request.AuthDingTalkAccountRequest;
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
import me.zhyd.oauth.request.AuthLineRequest;
import me.zhyd.oauth.request.AuthLinkedinRequest;
import me.zhyd.oauth.request.AuthMeituanRequest;
import me.zhyd.oauth.request.AuthMiRequest;
import me.zhyd.oauth.request.AuthMicrosoftRequest;
import me.zhyd.oauth.request.AuthOktaRequest;
import me.zhyd.oauth.request.AuthOschinaRequest;
import me.zhyd.oauth.request.AuthPinterestRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRenrenRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthSlackRequest;
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
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * AuthRequest工厂类
 * </p>
 */
public class AuthRequestFactory {

	private final JustAuthProperties properties;
	private final AuthStateCache authStateCache;

	public AuthRequestFactory(JustAuthProperties properties, AuthStateCache authStateCache) {
		this.properties = properties;
		this.authStateCache = authStateCache;
	}

	/**
	 * 返回当前Oauth列表
	 *
	 * @return Oauth列表
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List<String> oauthList() {
		// 默认列表
		List<String> defaultList = new ArrayList<>(properties.getType().keySet());
		// 扩展列表
		List<String> extendList = new ArrayList<>();
		ExtendProperties extend = properties.getExtend();
		if (null != extend) {
			Class enumClass = extend.getEnumClass();
			List<String> names = EnumUtil.getNames(enumClass);
			// 扩展列表
			extendList = extend.getConfig()
				.keySet()
				.stream()
				.map(String::toUpperCase)
				.filter(names::contains)
				.collect(Collectors.toList());
		}

		// 合并
		return (List<String>) CollUtil.addAll(defaultList, extendList);
	}

	/**
	 * 返回AuthRequest对象
	 *
	 * @param source {@link AuthSource}
	 * @return {@link AuthRequest}
	 */
	public AuthRequest get(String source) {
		if (StrUtil.isBlank(source)) {
			throw new AuthException(AuthResponseStatus.NO_AUTH_SOURCE);
		}

		// 获取 JustAuth 中已存在的
		AuthRequest authRequest = getDefaultRequest(source);

		// 如果获取不到则尝试取自定义的
		if (authRequest == null) {
			authRequest = getExtendRequest(properties.getExtend().getEnumClass(), source);
		}

		if (authRequest == null) {
			throw new AuthException(AuthResponseStatus.UNSUPPORTED);
		}

		return authRequest;
	}

	/**
	 * 获取自定义的 request
	 *
	 * @param clazz  枚举类 {@link AuthSource}
	 * @param source {@link AuthSource}
	 * @return {@link AuthRequest}
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private AuthRequest getExtendRequest(Class clazz, String source) {
		String upperSource = source.toUpperCase();
		try {
			EnumUtil.fromString(clazz, upperSource);
		} catch (IllegalArgumentException e) {
			// 无自定义匹配
			return null;
		}

		Map<String, ExtendProperties.ExtendRequestConfig> extendConfig = properties.getExtend()
			.getConfig();

		// key 转大写
		Map<String, ExtendProperties.ExtendRequestConfig> upperConfig = new HashMap<>(6);
		extendConfig.forEach((k, v) -> upperConfig.put(k.toUpperCase(), v));

		ExtendProperties.ExtendRequestConfig extendRequestConfig = upperConfig.get(upperSource);
		if (extendRequestConfig != null) {

			// 配置 http config
			configureHttpConfig(upperSource, extendRequestConfig, properties.getHttpConfig());

			Class<? extends AuthRequest> requestClass = extendRequestConfig.getRequestClass();

			if (requestClass != null) {
				// 反射获取 Request 对象，所以必须实现 2 个参数的构造方法
				return ReflectUtil.newInstance(requestClass, (AuthConfig) extendRequestConfig,
					authStateCache);
			}
		}

		return null;
	}


	/**
	 * 获取默认的 Request
	 *
	 * @param source {@link AuthSource}
	 * @return {@link AuthRequest}
	 */
	private AuthRequest getDefaultRequest(String source) {
		AuthDefaultSource authDefaultSource;

		try {
			authDefaultSource = EnumUtil.fromString(AuthDefaultSource.class, source.toUpperCase());
		} catch (IllegalArgumentException e) {
			// 无自定义匹配
			return null;
		}

		AuthConfig config = properties.getType().get(authDefaultSource.name());
		// 找不到对应关系，直接返回空
		if (config == null) {
			return null;
		}

		// 配置 http config
		configureHttpConfig(authDefaultSource.name(), config, properties.getHttpConfig());

		return switch (authDefaultSource) {
			case GITHUB -> new AuthGithubRequest(config, authStateCache);
			case WEIBO -> new AuthWeiboRequest(config, authStateCache);
			case GITEE -> new AuthGiteeRequest(config, authStateCache);
			case DINGTALK -> new AuthDingTalkRequest(config, authStateCache);
			case DINGTALK_ACCOUNT -> new AuthDingTalkAccountRequest(config, authStateCache);
			case BAIDU -> new AuthBaiduRequest(config, authStateCache);
			case CSDN -> new AuthCsdnRequest(config, authStateCache);
			case CODING -> new AuthCodingRequest(config, authStateCache);
			case OSCHINA -> new AuthOschinaRequest(config, authStateCache);
			case ALIPAY -> new AuthAlipayRequest(config, "", authStateCache);
			case QQ -> new AuthQqRequest(config, authStateCache);
			case WECHAT_OPEN -> new AuthWeChatOpenRequest(config, authStateCache);
			case WECHAT_MP -> new AuthWeChatMpRequest(config, authStateCache);
			case WECHAT_ENTERPRISE -> new AuthWeChatEnterpriseQrcodeRequest(config, authStateCache);
			case WECHAT_ENTERPRISE_WEB ->
				new AuthWeChatEnterpriseWebRequest(config, authStateCache);
			case TAOBAO -> new AuthTaobaoRequest(config, authStateCache);
			case GOOGLE -> new AuthGoogleRequest(config, authStateCache);
			case FACEBOOK -> new AuthFacebookRequest(config, authStateCache);
			case DOUYIN -> new AuthDouyinRequest(config, authStateCache);
			case LINKEDIN -> new AuthLinkedinRequest(config, authStateCache);
			case MICROSOFT -> new AuthMicrosoftRequest(config, authStateCache);
			case MI -> new AuthMiRequest(config, authStateCache);
			case TOUTIAO -> new AuthToutiaoRequest(config, authStateCache);
			case TEAMBITION -> new AuthTeambitionRequest(config, authStateCache);
			case RENREN -> new AuthRenrenRequest(config, authStateCache);
			case PINTEREST -> new AuthPinterestRequest(config, authStateCache);
			case STACK_OVERFLOW -> new AuthStackOverflowRequest(config, authStateCache);
			case HUAWEI -> new AuthHuaweiRequest(config, authStateCache);
			case GITLAB -> new AuthGitlabRequest(config, authStateCache);
			case KUJIALE -> new AuthKujialeRequest(config, authStateCache);
			case ELEME -> new AuthElemeRequest(config, authStateCache);
			case MEITUAN -> new AuthMeituanRequest(config, authStateCache);
			case TWITTER -> new AuthTwitterRequest(config, authStateCache);
			case FEISHU -> new AuthFeishuRequest(config, authStateCache);
			case JD -> new AuthJdRequest(config, authStateCache);
			case ALIYUN -> new AuthAliyunRequest(config, authStateCache);
			case XMLY -> new AuthXmlyRequest(config, authStateCache);
			case AMAZON -> new AuthAmazonRequest(config, authStateCache);
			case SLACK -> new AuthSlackRequest(config, authStateCache);
			case LINE -> new AuthLineRequest(config, authStateCache);
			case OKTA -> new AuthOktaRequest(config, authStateCache);
			default -> null;
		};
	}

	/**
	 * 配置 http 相关的配置
	 *
	 * @param authSource {@link AuthSource}
	 * @param authConfig {@link AuthConfig}
	 */
	private void configureHttpConfig(String authSource, AuthConfig authConfig,
		JustAuthProperties.JustAuthHttpConfig httpConfig) {
		if (null == httpConfig) {
			return;
		}
		Map<String, JustAuthProperties.JustAuthProxyConfig> proxyConfigMap = httpConfig.getProxy();
		if (CollectionUtils.isEmpty(proxyConfigMap)) {
			return;
		}
		JustAuthProperties.JustAuthProxyConfig proxyConfig = proxyConfigMap.get(authSource);

		if (null == proxyConfig) {
			return;
		}

		authConfig.setHttpConfig(HttpConfig.builder()
			.timeout(httpConfig.getTimeout())
			.proxy(new Proxy(Proxy.Type.valueOf(proxyConfig.getType()),
				new InetSocketAddress(proxyConfig.getHostname(), proxyConfig.getPort())))
			.build());
	}
}
