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

package com.taotao.cloud.gateway.service;

import com.taotao.cloud.gateway.model.BlackList;
import java.util.Set;

/**
 * 规则缓存业务
 */
public interface IRuleCacheService {

    /**
     * 根据IP获取黑名单
     *
     * @param ip 　ip
     * @return Set
     */
    Set<Object> getBlackList(String ip);

    /**
     * 查询所有黑名单
     *
     * @return Set
     */
    Set<Object> getBlackList();

    /**
     * 设置黑名单
     *
     * @param blackList 黑名单对象
     */
    void setBlackList(BlackList blackList);

    /**
     * 删除黑名单
     *
     * @param blackList 黑名单对象
     */
    void deleteBlackList(BlackList blackList);
}
