/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2;

import com.taotao.cloud.auth.biz.authentication.processor.HttpCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.utils.OAuth2EndpointUtils;
import com.taotao.cloud.auth.biz.utils.SessionInvalidException;
import com.taotao.cloud.security.springsecurity.core.constants.OAuth2ErrorKeys;
import com.taotao.cloud.security.springsecurity.core.utils.ListUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.List;

/**
 * <p>Description: 抽象的认证 Converter </p>
 */
public abstract class AbstractAuthenticationConverter implements AuthenticationConverter {

	private final HttpCryptoProcessor httpCryptoProcessor;

	public AbstractAuthenticationConverter(HttpCryptoProcessor httpCryptoProcessor) {
		this.httpCryptoProcessor = httpCryptoProcessor;
	}

	protected String[] decrypt(String sessionId, List<String> parameters) {
		if (StringUtils.isNotBlank(sessionId) && CollectionUtils.isNotEmpty(parameters)) {
			List<String> result = parameters.stream().map(item -> decrypt(sessionId, item)).toList();
			return ListUtils.toStringArray(result);
		}

		return ListUtils.toStringArray(parameters);
	}

	protected String decrypt(String sessionId, String parameter) {
		if (StringUtils.isNotBlank(sessionId) && StringUtils.isNotBlank(parameter)) {
			try {
				return httpCryptoProcessor.decrypt(sessionId, parameter);
			} catch (SessionInvalidException e) {
				OAuth2EndpointUtils.throwError(
					OAuth2ErrorKeys.SESSION_EXPIRED,
					e.getMessage(),
					OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
			}
		}
		return parameter;
	}
}
