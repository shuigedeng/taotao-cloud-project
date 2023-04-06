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

package com.taotao.cloud.auth.biz.authentication.oauth2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

public class DelegateClientRegistrationRepository implements ClientRegistrationRepository {
    private Function<String, ClientRegistration> delegate;
    private final Map<String, ClientRegistration> clientRegistrationMap = new HashMap<>();

    public void setDelegate(Function<String, ClientRegistration> delegate) {
        this.delegate = delegate;
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        if (clientRegistrationMap.containsKey(registrationId)) {
            return clientRegistrationMap.get(registrationId);
        }
        return delegate.apply(registrationId);
    }

    public Map<String, ClientRegistration> getClientRegistrationMap() {
        return clientRegistrationMap;
    }

    public void addClientRegistration(ClientRegistration clientRegistration) {
        clientRegistrationMap.putIfAbsent(clientRegistration.getRegistrationId(), clientRegistration);
    }
}
