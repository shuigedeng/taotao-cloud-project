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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.converter;

import com.taotao.cloud.auth.infrastructure.persistent.authorization.jackson2.OAuth2JacksonProcessor;
import com.taotao.cloud.auth.infrastructure.persistent.authorization.po.TtcRegisteredClient;
import java.util.Map;
import java.util.Set;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

/**
 * <p>TtcRegisteredClient 转 换适配器 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:13:50
 */
public class TtcToOAuth2RegisteredClientConverter
	extends AbstractRegisteredClientConverter<TtcRegisteredClient> {

	/**
	 * Ttc到oauth2注册客户端转换器
	 *
	 * @param jacksonProcessor 杰克逊处理器
	 * @return
	 * @since 2023-07-10 17:13:50
	 */
	public TtcToOAuth2RegisteredClientConverter(OAuth2JacksonProcessor jacksonProcessor) {
		super(jacksonProcessor);
	}

	/**
	 * 获取范围
	 *
	 * @param details 详细信息
	 * @return {@link Set }<{@link String }>
	 * @since 2023-07-10 17:13:50
	 */
	@Override
	public Set<String> getScopes(TtcRegisteredClient details) {
		return StringUtils.commaDelimitedListToSet(details.getScopes());
	}

	/**
	 * 获取客户端设置
	 *
	 * @param details 详细信息
	 * @return {@link ClientSettings }
	 * @since 2023-07-10 17:13:51
	 */
	@Override
	public ClientSettings getClientSettings(TtcRegisteredClient details) {
		Map<String, Object> clientSettingsMap = parseMap(details.getClientSettings());
		return ClientSettings.withSettings(clientSettingsMap).build();
	}

	/**
	 * 获取令牌设置
	 *
	 * @param details 详细信息
	 * @return {@link TokenSettings }
	 * @since 2023-07-10 17:13:51
	 */
	@Override
	public TokenSettings getTokenSettings(TtcRegisteredClient details) {
		Map<String, Object> tokenSettingsMap = parseMap(details.getTokenSettings());
		return TokenSettings.withSettings(tokenSettingsMap).build();
	}
}
