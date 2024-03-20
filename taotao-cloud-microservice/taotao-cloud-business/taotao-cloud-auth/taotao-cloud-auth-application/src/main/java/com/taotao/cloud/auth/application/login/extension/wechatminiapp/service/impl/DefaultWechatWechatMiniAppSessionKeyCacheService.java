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

package com.taotao.cloud.auth.application.login.extension.wechatminiapp.service.impl;

import com.taotao.cloud.auth.application.login.extension.wechatminiapp.service.WechatMiniAppSessionKeyCacheService;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** // 小程序sessionkey缓存 过期时间应该小于微信官方文档的声明 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置 */
@Service
public class DefaultWechatWechatMiniAppSessionKeyCacheService implements WechatMiniAppSessionKeyCacheService {

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public String put(String cacheKey, String sessionKey) {
        redisRepository.set(cacheKey, sessionKey);
        return sessionKey;
    }

    @Override
    public String get(String cacheKey) {
        // 模拟 sessionkey 缓存
        return (String) redisRepository.get(cacheKey);
    }
}
