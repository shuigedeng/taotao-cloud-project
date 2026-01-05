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

package com.taotao.cloud.mq.common.balance;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.common.balance.impl.LoadBalances;

import java.util.ArrayList;
import java.util.List;

/**
 * LoadBalanceBs
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public final class LoadBalanceBs<T extends Server> {

    private LoadBalance<T> loadBalance = LoadBalances.random();
    private String hashKey = "";
    private List<T> servers = new ArrayList();

    private LoadBalanceBs() {
    }

    public static <T extends Server> LoadBalanceBs<T> newInstance() {
        return new LoadBalanceBs<T>();
    }

    public LoadBalanceBs<T> loadBalance( LoadBalance<T> loadBalance ) {
        ArgUtils.notNull(loadBalance, "loadBalance");
        this.loadBalance = loadBalance;
        return this;
    }

    public LoadBalanceBs<T> hashKey( String hashKey ) {
        this.hashKey = hashKey;
        return this;
    }

    public LoadBalanceBs<T> servers( List<T> servers ) {
        ArgUtils.notEmpty(servers, "servers");
        this.servers = servers;
        return this;
    }

    public T select() {
        ArgUtils.notEmpty(this.servers, "servers");
        ArgUtils.notNull(this.loadBalance, "loadBalance");
        LoadBalanceContext<T> context =
                com.taotao.cloud.mq.common.balance.impl.LoadBalanceContext.<T>newInstance().hashKey(this.hashKey).servers(this.servers);
        return (T) this.loadBalance.select(context);
    }
}
