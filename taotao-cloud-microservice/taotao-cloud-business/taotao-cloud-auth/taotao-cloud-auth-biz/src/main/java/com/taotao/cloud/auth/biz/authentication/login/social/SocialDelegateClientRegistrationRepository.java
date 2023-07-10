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

package com.taotao.cloud.auth.biz.authentication.login.social;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * 社会委托客户注册存储库
 *
 * @author shuigedeng
 * @version 2023.07
 * @see ClientRegistrationRepository
 * @since 2023-07-10 17:40:43
 */
public class SocialDelegateClientRegistrationRepository implements ClientRegistrationRepository {
	/**
	 * 委托
	 */
	private Function<String, ClientRegistration> delegate;
	/**
	 * 客户注册地图
	 */
	private final Map<String, ClientRegistration> clientRegistrationMap = new HashMap<>();

	/**
	 * 设置委托
	 *
	 * @param delegate 委托
	 * @since 2023-07-10 17:40:44
	 */
	public void setDelegate(Function<String, ClientRegistration> delegate) {
        this.delegate = delegate;
    }

	/**
	 * 通过注册id查找
	 *
	 * @param registrationId 注册id
	 * @return {@link ClientRegistration }
	 * @since 2023-07-10 17:40:44
	 */
	@Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        if (clientRegistrationMap.containsKey(registrationId)) {
            return clientRegistrationMap.get(registrationId);
        }
        return delegate.apply(registrationId);
    }

	/**
	 * 获取客户注册地图
	 *
	 * @return {@link Map }<{@link String }, {@link ClientRegistration }>
	 * @since 2023-07-10 17:40:44
	 */
	public Map<String, ClientRegistration> getClientRegistrationMap() {
        return clientRegistrationMap;
    }

	/**
	 * 添加客户端注册
	 *
	 * @param clientRegistration 客户注册
	 * @since 2023-07-10 17:40:45
	 */
	public void addClientRegistration(ClientRegistration clientRegistration) {
        clientRegistrationMap.putIfAbsent(clientRegistration.getRegistrationId(), clientRegistration);
    }
}
