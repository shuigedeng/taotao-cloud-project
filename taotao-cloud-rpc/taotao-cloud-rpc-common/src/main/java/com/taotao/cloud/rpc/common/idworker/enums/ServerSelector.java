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

package com.taotao.cloud.rpc.common.idworker.enums;

/**
 * 锁常量
 */
public enum ServerSelector {
    // Redis 服务
    REDIS_SERVER(0),
    ZOOKEEPER_SERVER(1),
    CACHE_SERVER(2);

    private final Integer code;

    ServerSelector(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
