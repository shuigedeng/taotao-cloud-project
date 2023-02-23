/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.demo.authorization.processor;

import cn.herodotus.engine.assistant.core.definition.constants.SymbolConstants;
import cn.herodotus.engine.oauth2.authorization.definition.HerodotusConfigAttribute;
import cn.herodotus.engine.oauth2.authorization.definition.HerodotusRequestMatcher;
import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusGrantedAuthority;
import cn.herodotus.engine.oauth2.core.definition.domain.SecurityAttribute;
import cn.herodotus.engine.oauth2.core.enums.PermissionExpression;
import cn.herodotus.engine.web.core.domain.RequestMapping;
import cn.hutool.core.util.ReUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

/**
 * <p>Description: 表达式权限解析器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/31 17:13
 */
public class SecurityMetadataSourceParser {

	private static final Logger log = LoggerFactory.getLogger(SecurityMetadataSourceParser.class);

	private final SecurityMatcherConfigurer securityMatcherConfigurer;

	public SecurityMetadataSourceParser(SecurityMatcherConfigurer securityMatcherConfigurer) {
		this.securityMatcherConfigurer = securityMatcherConfigurer;
	}

	/**
	 * 通过反射，实现
	 * {@link
	 * org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource}
	 * 类中 {@code requestMap } 数据的读取。 通过antMatcher等方法配置的权限，均在 {@code requestMap } 中存储
	 *
	 * @return requestMap 中存储的权限数据
	 */
	public LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> getConfiguredSecurityMetadata() {

		List<String> permitAllMatcher = securityMatcherConfigurer.getPermitAllList();

		if (CollectionUtils.isNotEmpty(permitAllMatcher)) {
			LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> result = new LinkedHashMap<>();
			permitAllMatcher.forEach(item -> {
				result.put(new HerodotusRequestMatcher(item),
					new HerodotusConfigAttribute(PermissionExpression.PERMIT_ALL.getValue()));
			});
			return result;
		}
		return new LinkedHashMap<>();
	}

	/**
	 * 将 RequestMatcher 强制转换为 HerodotusRequestMatcher 方便使用。
	 * <p>
	 * 1. 这里转换的内容，主要来源于 SecurityFilterChain 的 antMatchers 配置的权限过滤内容 2. SecurityFilterChain 中的配置
	 * RequestMatcher 的主要类型是 AntPathRequestMatcher，当然还有其它类型，通常不用作为权限数据，所以此处只处理 AntPathRequestMatcher
	 * 类型。 3. 这里的转换，主要是因为 （1） AntPathRequestMatcher 只支持 HttpRequest 的解析，无法支持字符串路径。 （2）以
	 * AntPathRequestMatcher 作为缓存的Key，默认以该对象的json进行存储，因该类中未提供 method的get方法，所以 method
	 * 信息无法获取到。同时，转成的json不包含 method 信息。
	 * <p>
	 * 因此，这里只能通过解析 AntPathRequestMatcher toString() 方法中的信息，再拼凑成自己自定义的 RequestMatcher对象。
	 * AntPathRequestMatcher toString() 主要的结果是： 1. 没有指定 HttpMethod：Ant [pattern='/xxx/xx'] 2. 指定了
	 * HttpMethod：Ant [pattern='/xxx/xx', GET]
	 *
	 * @param requestMatcher SecurityFilterChain 的 antMatchers 配置
	 * @return 自定义 RequestMatcher
	 */
	@Deprecated
	private HerodotusRequestMatcher convert(RequestMatcher requestMatcher) {
		if (requestMatcher instanceof AntPathRequestMatcher) {
			AntPathRequestMatcher antPathRequestMatcher = (AntPathRequestMatcher) requestMatcher;
			String toStringValue = antPathRequestMatcher.toString();
			log.debug("[Herodotus] |- Request matcher content is [{}]", toStringValue);

			// TODO：2022-03-11 正则表达式都忘光了，这里临时用以下方法进行解析
			String regex = "\\w+\\s*=\\s*'(.*?)'";
			if (StringUtils.contains(toStringValue, SymbolConstants.COMMA)) {
				regex = "\\w+\\s*=\\s*'(.*?)',\\s*(\\w+)";
			}

			String pattern = null;
			String httpMethod = null;
			List<String> values = ReUtil.getAllGroups(Pattern.compile(regex), toStringValue, false);
			if (CollectionUtils.isNotEmpty(values)) {
				pattern = values.get(0);
				if (values.size() >= 2) {
					httpMethod = values.get(1);
				}
				log.trace(
					"[Herodotus] |- Parse the request matcher value with regex is pattern: [{}], method: [{}]",
					pattern, httpMethod);
				return new HerodotusRequestMatcher(pattern, httpMethod);
			}
		}

		return null;
	}

	/**
	 * 直接使用
	 * {@link
	 * org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer}
	 * 中的方法
	 *
	 * @param rolePrefix  角色权限的前缀
	 * @param authorities 权限
	 * @return 权限表达式
	 */
	private static String hasAnyRole(String rolePrefix, String... authorities) {
		String anyAuthorities = org.springframework.util.StringUtils.arrayToDelimitedString(
			authorities, "','" + rolePrefix);
		return "hasAnyRole('" + rolePrefix + anyAuthorities + "')";
	}

	/**
	 * 直接使用
	 * {@link
	 * org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer}
	 * 中的方法
	 *
	 * @param rolePrefix 角色权限的前缀
	 * @param role       角色权限名称
	 * @return 权限表达式
	 */
	private static String hasRole(String rolePrefix, String role) {
		Assert.notNull(role, "role cannot be null");
		Assert.isTrue(rolePrefix.isEmpty() || !role.startsWith(rolePrefix),
			() -> "role should not start with '"
				+ rolePrefix + "' since it is automatically inserted. Got '" + role + "'");
		return "hasRole('" + rolePrefix + role + "')";
	}

	/**
	 * 直接使用
	 * {@link
	 * org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer}
	 * 中的方法
	 *
	 * @param authority 权限
	 * @return 权限表达式
	 */
	private static String hasAuthority(String authority) {
		return "hasAuthority('" + authority + "')";
	}

	/**
	 * 直接使用
	 * {@link
	 * org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer}
	 * 中的方法
	 *
	 * @param authorities 权限
	 * @return 权限表达式
	 */
	private static String hasAnyAuthority(String... authorities) {
		String anyAuthorities = org.springframework.util.StringUtils.arrayToDelimitedString(
			authorities, "','");
		return "hasAnyAuthority('" + anyAuthorities + "')";
	}

	/**
	 * 直接使用
	 * {@link
	 * org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer}
	 * 中的方法
	 *
	 * @param ipAddressExpression ip地址表达式
	 * @return 权限表达式
	 */
	private static String hasIpAddress(String ipAddressExpression) {
		return "hasIpAddress('" + ipAddressExpression + "')";
	}

	@NotNull
	private String createExpression(SecurityAttribute securityAttribute, String code) {
		return getExpression(securityAttribute) + "('" + code + "')";
	}

	/**
	 * 创建请求和权限的映射数据
	 *
	 * @param securityAttribute {@link SecurityAttribute}
	 * @return 保存请求和权限的映射的Map
	 */
	public LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> postProcess(
		SecurityAttribute securityAttribute) {
		return convertToSecurityMetadata(securityAttribute.getUrl(),
			securityAttribute.getRequestMethod(), analysis(securityAttribute));
	}

	/**
	 * 创建请求和默认权限的映射数据
	 *
	 * @param requestMapping {@link RequestMapping}
	 * @return 请求和权限的映射的Map
	 */
	public LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> postProcess(
		RequestMapping requestMapping) {
		return convertToSecurityMetadata(requestMapping.getUrl(), requestMapping.getRequestMethod(),
			hasAuthority(requestMapping.getMetadataCode()));
	}

	/**
	 * 解析并动态组装所需要的权限。
	 * <p>
	 * 1. 原 spring-security-oauth Spring Security
	 * 基础权限规则，来源于org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl
	 * OAuth2 权限规则来源于
	 * org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods 2. 新
	 * spring-authorization-server Spring Security
	 * 基础权限规则，来源于{@link
	 * org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer}
	 * OAuth2 权限规则来源于 目前还没有
	 * <p>
	 * 具体解析采用的是 Security 的 {@link org.springframework.security.access.AccessDecisionVoter}
	 * 方式，而不是采用自定义的 {@link org.springframework.security.access.AccessDecisionManager} 该方式会与默认的
	 * httpsecurity 配置覆盖。 · 基本的权限验证采用的是：{@link org.springframework.security.access.vote.RoleVoter} ·
	 * scope权限采用两种方式： 一种是：Spring Security
	 * org.springframework.security.oauth2.provider.vote.ScopeVoter 目前已取消 另一种是：OAuth2
	 * 'hasScope'和'hasAnyScope'方式
	 * org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods#hasAnyScope(String...)
	 * <p>
	 * 如果实际应用不满足可以，自己扩展AccessDecisionVoter或者AccessDecisionManager
	 *
	 * @param securityAttribute {@link SecurityAttribute}
	 * @return security权限定义集合
	 */
	private HerodotusConfigAttribute analysis(SecurityAttribute securityAttribute) {

        /*
          如果使用Spring Security基础的字符串权限，那么其它权限则不需要设置。

          默认字符串权限包括以下：
          · permitAll
          · denyAll
          · anonymous
          · authenticated
          · fullyAuthenticated
          · rememberMe
          · !permitAll
          · !denyAll
          · !anonymous
          · !authenticated
          · !fullyAuthenticated
          · !rememberMe

          {@link org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer}
         */
		if (StringUtils.isNotBlank(securityAttribute.getManualSetting())) {
			return HerodotusConfigAttribute.create(securityAttribute.getManualSetting());
		}

        /*
          如果使用ip地址表达式，那么使用{@link org.springframework.security.web.access.expression.WebExpressionVoter} 对权限进行验证。

          ip 表达式 {@link org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer#hasIpAddress(String)}

          注意：IP表达式应该与Srping Security基础表达式是互斥的，因为如果同时使用，逻辑上会有冲突。所以只能二选一。
         */
		if (StringUtils.isNotBlank(securityAttribute.getIpAddress())) {
			return HerodotusConfigAttribute.create(hasIpAddress(securityAttribute.getIpAddress()));
		}

		if (StringUtils.isNotBlank(securityAttribute.getExpression())) {
			String staticExpression = getExpression(securityAttribute);
			if (StringUtils.endsWithIgnoreCase(staticExpression, "Role")) {
				String expression = createRoleExpression(securityAttribute);
				if (StringUtils.isNotBlank(expression)) {
					return HerodotusConfigAttribute.create(expression);
				}
			}

			return HerodotusConfigAttribute.create(staticExpression);
		}

		return HerodotusConfigAttribute.create(hasAuthority(securityAttribute.getAttributeCode()));
	}

	/**
	 * 根据 authority_code 和 请求的 url、method 创建创建请求和默认权限的映射数据
	 *
	 * @param url        请求url
	 * @param methods    请求method
	 * @param expression 权限代码{@link GrantedAuthority#getAuthority()}
	 * @return 请求和权限的映射的Map
	 */
	private LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> convertToSecurityMetadata(
		String url, String methods, String expression) {
		return this.convertToSecurityMetadata(url, methods,
			new HerodotusConfigAttribute(expression));
	}

	/**
	 * 创建请求和权限的映射数据
	 *
	 * @param url                      请求url
	 * @param methods                  请求method
	 * @param herodotusConfigAttribute Security权限{@link ConfigAttribute}
	 * @return 保存请求和权限的映射的Map
	 */
	private LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> convertToSecurityMetadata(
		String url, String methods, HerodotusConfigAttribute herodotusConfigAttribute) {
		LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> result = new LinkedHashMap<>();
		if (StringUtils.isBlank(methods)) {
			result.put(new HerodotusRequestMatcher(url), herodotusConfigAttribute);
		} else {
			// 如果methods是以逗号分隔的字符串，那么进行拆分处理
			if (StringUtils.contains(methods, SymbolConstants.COMMA)) {
				String[] multiMethod = StringUtils.split(methods, SymbolConstants.COMMA);
				for (String method : multiMethod) {
					result.put(new HerodotusRequestMatcher(url, method), herodotusConfigAttribute);
				}
			} else {
				result.put(new HerodotusRequestMatcher(url, methods), herodotusConfigAttribute);
			}
		}

		return result;
	}

	private String getExpression(SecurityAttribute securityAttribute) {
		return PermissionExpression.valueOf(securityAttribute.getExpression()).getValue();
	}

	private String createRoleExpression(SecurityAttribute securityAttribute) {
		Set<HerodotusGrantedAuthority> roles = securityAttribute.getRoles();
		return createExpression(securityAttribute, roles);
	}

	@Nullable
	private String createExpression(SecurityAttribute securityAttribute,
		Set<HerodotusGrantedAuthority> relations) {
		if (CollectionUtils.isNotEmpty(relations)) {
			if (StringUtils.containsIgnoreCase(getExpression(securityAttribute), "Any")) {
				String code = relations.stream().map(HerodotusGrantedAuthority::getAuthority)
					.collect(Collectors.joining(SymbolConstants.COMMA));
				return createExpression(securityAttribute, code);
			} else {
				Optional<HerodotusGrantedAuthority> optional = relations.stream().findFirst();
				if (optional.isPresent()) {
					String code = optional.get().getAuthority();
					return createExpression(securityAttribute, code);
				}
			}
		}

		return null;
	}
}
