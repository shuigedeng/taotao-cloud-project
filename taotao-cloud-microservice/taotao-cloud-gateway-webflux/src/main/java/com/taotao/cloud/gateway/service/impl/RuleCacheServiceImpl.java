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

package com.taotao.cloud.gateway.service.impl;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.cloud.gateway.model.BlackList;
import com.taotao.cloud.gateway.model.RuleConstant;
import com.taotao.cloud.gateway.service.IRuleCacheService;
import java.util.Set;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 规则缓存实现业务类
 */
@Service
@AllArgsConstructor
public class RuleCacheServiceImpl implements IRuleCacheService {

    private final RedisRepository repository;

    @Override
    public Set<Object> getBlackList(String ip) {
        return repository.sGet(RuleConstant.getBlackListCacheKey(ip));
    }

    @Override
    public Set<Object> getBlackList() {
        return repository.sGet(RuleConstant.getBlackListCacheKey());
    }

    @Override
    public void setBlackList(BlackList blackList) {
        String key =
                StringUtils.isNotBlank(blackList.getIp())
                        ? RuleConstant.getBlackListCacheKey(blackList.getIp())
                        : RuleConstant.getBlackListCacheKey();
        repository.sSet(key, JacksonUtils.toJSONString(blackList));
    }

    @Override
    public void deleteBlackList(BlackList blackList) {
        String key =
                StringUtils.isNotBlank(blackList.getIp())
                        ? RuleConstant.getBlackListCacheKey(blackList.getIp())
                        : RuleConstant.getBlackListCacheKey();
        repository.setRemove(key, JacksonUtils.toJSONString(blackList));
    }
}
