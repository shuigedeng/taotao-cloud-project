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

import com.taotao.cloud.mq.common.balance.ILoadBalanceContext;
import com.taotao.cloud.mq.common.balance.IServer;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class LoadBalanceRoundRobbin<T extends IServer> extends AbstractLoadBalance<T> {
    private final AtomicLong indexHolder = new AtomicLong();

    protected T doSelect(ILoadBalanceContext<T> context) {
        List<T> servers = context.servers();
        long index = this.indexHolder.getAndIncrement();
        int actual = (int) (index % (long) servers.size());
        return (T) (servers.get(actual));
    }
}
