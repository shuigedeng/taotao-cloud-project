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

import com.taotao.cloud.mq.common.balance.LoadBalance;
import com.taotao.cloud.mq.common.balance.LoadBalanceContext;
import com.taotao.cloud.mq.common.balance.Server;

import java.util.List;

/**
 * AbstractLoadBalance
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public abstract class AbstractLoadBalance<T extends Server> implements LoadBalance<T> {

    public T select( LoadBalanceContext<T> context ) {
        List<T> servers = context.servers();
        return (T) ( servers.size() <= 1 ? (Server) servers.get(0) : this.doSelect(context) );
    }

    protected abstract T doSelect( LoadBalanceContext<T> var1 );
}
