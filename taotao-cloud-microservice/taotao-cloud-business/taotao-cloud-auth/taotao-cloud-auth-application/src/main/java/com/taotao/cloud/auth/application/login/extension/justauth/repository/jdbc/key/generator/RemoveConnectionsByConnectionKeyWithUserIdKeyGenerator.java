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

package com.taotao.cloud.auth.application.login.extension.justauth.repository.jdbc.key.generator;

import com.taotao.cloud.auth.application.login.extension.justauth.entity.ConnectionKey;
import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;

/**
 * @author YongWu zheng
 * @version V2.0  Created by 2020/6/14 21:07
 */
public class RemoveConnectionsByConnectionKeyWithUserIdKeyGenerator implements KeyGenerator {

    @NonNull
    @Override
    public Object generate(@NonNull Object target, @NonNull Method method, Object... params) {
        String userId = (String) params[0];
        ConnectionKey key = (ConnectionKey) params[1];
        //		return "h:" + userId + RedisCacheAutoConfiguration.REDIS_CACHE_KEY_SEPARATE +
        //			key.getProviderId() + RedisCacheAutoConfiguration.REDIS_CACHE_HASH_KEY_SEPARATE + key.getProviderUserId();
        return "sfasdfas";
    }
}
