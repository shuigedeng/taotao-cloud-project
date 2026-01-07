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

package com.taotao.cloud.mq.common.balance.impl;

import com.taotao.cloud.mq.common.balance.LoadBalanceContext;
import com.taotao.cloud.mq.common.balance.Server;
import com.taotao.boot.common.support.hash.ConsistentHashingBs;
import com.taotao.boot.common.support.hash.ConsistentHashing;
import com.taotao.boot.common.support.hash.api.HashCode;

/**
 * LoadBalanceConsistentHash
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class LoadBalanceConsistentHash<T extends Server> extends AbstractLoadBalanceHash<T> {

    public LoadBalanceConsistentHash( HashCode hashCode ) {
        super(hashCode);
    }

    protected T doSelect( LoadBalanceContext<T> context ) {
        ConsistentHashing<T> consistentHashing =
                ConsistentHashingBs.<T>newInstance()
                        .hashCode(this.hashCode)
                        .nodes(context.servers())
                        .build();
        String hashKey = context.hashKey();
        return (T) ( consistentHashing.get(hashKey) );
    }
}
