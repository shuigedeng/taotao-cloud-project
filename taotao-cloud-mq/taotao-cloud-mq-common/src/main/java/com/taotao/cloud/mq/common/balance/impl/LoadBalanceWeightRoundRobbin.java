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

import com.taotao.boot.common.support.filter.Filter;
import com.taotao.boot.common.support.handler.Handler;
import com.taotao.boot.common.utils.collection.CollectionUtils;
import com.taotao.boot.common.utils.number.MathUtils;
import com.taotao.cloud.mq.common.balance.LoadBalanceContext;
import com.taotao.cloud.mq.common.balance.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LoadBalanceWeightRoundRobbin
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class LoadBalanceWeightRoundRobbin<T extends Server> extends AbstractLoadBalance<T> {

    private final AtomicLong indexHolder = new AtomicLong();

    protected T doSelect( LoadBalanceContext<T> context ) {
        List<T> servers = context.servers();
        List<T> actualList = this.buildActualList(servers);
        long index = this.indexHolder.getAndIncrement();
        int actual = (int) ( index % (long) actualList.size() );
        return (T) ( actualList.get(actual) );
    }

    private List<T> buildActualList( List<T> serverList ) {
        List<T> actualList = new ArrayList();
        List<T> notZeroServers =
                CollectionUtils.filterList(
                        serverList,
                        new Filter<T>() {
                            public boolean filter( Server iServer ) {
                                return iServer.weight() <= 0;
                            }
                        });
        List<Integer> weightList =
                CollectionUtils.toList(
                        notZeroServers,
                        new Handler<T, Integer>() {
                            public Integer handle( T iServer ) {
                                return iServer.weight();
                            }
                        });
        int maxDivisor = MathUtils.ngcd(weightList);

        for (T server : notZeroServers) {
            int weight = server.weight();
            int times = weight / maxDivisor;

            for (int i = 0; i < times; ++i) {
                actualList.add(server);
            }
        }

        return actualList;
    }
}
