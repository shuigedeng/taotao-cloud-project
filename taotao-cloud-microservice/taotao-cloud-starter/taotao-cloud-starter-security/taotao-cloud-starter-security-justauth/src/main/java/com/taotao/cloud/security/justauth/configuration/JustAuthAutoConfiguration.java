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

package com.taotao.cloud.security.justauth.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.security.justauth.factory.AuthRequestFactory;
import com.taotao.cloud.security.justauth.properties.JustAuthProperties;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * JustAuth 自动装配类
 * </p>
 */
@AutoConfiguration
@EnableConfigurationProperties(JustAuthProperties.class)
public class JustAuthAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(JustAuthAutoConfiguration.class, StarterName.SECURITY_JUSTAUTH_STARTER);
	}

	@Bean
	@ConditionalOnProperty(prefix = "justauth", value = "enabled", havingValue = "true", matchIfMissing = true)
	public AuthRequestFactory authRequestFactory(JustAuthProperties properties,
		AuthStateCache authStateCache) {
		return new AuthRequestFactory(properties, authStateCache);
	}

	@Configuration
	@Import({JustAuthStateCacheConfiguration.Default.class,
		JustAuthStateCacheConfiguration.Redis.class, JustAuthStateCacheConfiguration.Custom.class})
	protected static class AuthStateCacheAutoConfiguration {

	}

}
