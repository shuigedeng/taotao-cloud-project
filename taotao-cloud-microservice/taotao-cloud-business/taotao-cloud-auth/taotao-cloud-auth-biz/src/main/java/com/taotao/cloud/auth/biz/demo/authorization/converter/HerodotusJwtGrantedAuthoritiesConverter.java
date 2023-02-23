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

package com.taotao.cloud.auth.biz.demo.authorization.converter;

import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusGrantedAuthority;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * <p>Description: Jwt 权限数据转换器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/13 14:21
 */
public class HerodotusJwtGrantedAuthoritiesConverter implements
	Converter<Jwt, Collection<GrantedAuthority>> {

	private final Log logger = LogFactory.getLog(getClass());

	private static final String DEFAULT_AUTHORITY_PREFIX = "SCOPE_";

	private static final Collection<String> WELL_KNOWN_AUTHORITIES_CLAIM_NAMES = Arrays.asList(
		"scope", "scp");

	private String authorityPrefix = DEFAULT_AUTHORITY_PREFIX;

	private String authoritiesClaimName;

	/**
	 * Extract {@link GrantedAuthority}s from the given {@link Jwt}.
	 *
	 * @param jwt The {@link Jwt} token
	 * @return The {@link GrantedAuthority authorities} read from the token scopes
	 */
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String authority : getAuthorities(jwt)) {
			grantedAuthorities.add(new HerodotusGrantedAuthority(authority));
		}
		return grantedAuthorities;
	}

	/**
	 * Sets the prefix to use for {@link GrantedAuthority authorities} mapped by this converter.
	 * Defaults to
	 *
	 * @param authorityPrefix The authority prefix
	 * @since 5.2
	 */
	public void setAuthorityPrefix(String authorityPrefix) {
		Assert.notNull(authorityPrefix, "authorityPrefix cannot be null");
		this.authorityPrefix = authorityPrefix;
	}

	/**
	 * Sets the name of token claim to use for mapping {@link GrantedAuthority authorities} by this
	 * converter. Defaults to
	 *
	 * @param authoritiesClaimName The token claim name to map authorities
	 * @since 5.2
	 */
	public void setAuthoritiesClaimName(String authoritiesClaimName) {
		Assert.hasText(authoritiesClaimName, "authoritiesClaimName cannot be empty");
		this.authoritiesClaimName = authoritiesClaimName;
	}

	private String getAuthoritiesClaimName(Jwt jwt) {
		if (this.authoritiesClaimName != null) {
			return this.authoritiesClaimName;
		}
		for (String claimName : WELL_KNOWN_AUTHORITIES_CLAIM_NAMES) {
			if (jwt.hasClaim(claimName)) {
				return claimName;
			}
		}
		return null;
	}

	private Collection<String> getAuthorities(Jwt jwt) {
		String claimName = getAuthoritiesClaimName(jwt);
		if (claimName == null) {
			this.logger.trace(
				"Returning no authorities since could not find any claims that might contain scopes");
			return Collections.emptyList();
		}
		if (this.logger.isTraceEnabled()) {
			this.logger.trace(LogMessage.format("Looking for scopes in claim %s", claimName));
		}
		Object authorities = jwt.getClaim(claimName);
		if (authorities instanceof String) {
			if (StringUtils.hasText((String) authorities)) {
				return Arrays.asList(((String) authorities).split(" "));
			}
			return Collections.emptyList();
		}
		if (authorities instanceof Collection) {
			return castAuthoritiesToCollection(authorities);
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	private Collection<String> castAuthoritiesToCollection(Object authorities) {
		return (Collection<String>) authorities;
	}
}
