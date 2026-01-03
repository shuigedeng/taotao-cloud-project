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

/**
 * LoadBalanceContext
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class LoadBalanceContext<T extends IServer> implements ILoadBalanceContext<T> {

    private String hashKey;
    private List<T> servers;

    public static <T extends IServer> LoadBalanceContext<T> newInstance() {
        return new LoadBalanceContext<T>();
    }

    public String hashKey() {
        return this.hashKey;
    }

    public LoadBalanceContext<T> hashKey( String hashKey ) {
        this.hashKey = hashKey;
        return this;
    }

    public List<T> servers() {
        return this.servers;
    }

    public LoadBalanceContext<T> servers( List<T> servers ) {
        this.servers = servers;
        return this;
    }

    public String toString() {
        return "LoadBalanceContext{hashKey='"
                + this.hashKey
                + '\''
                + ", servers="
                + this.servers
                + '}';
    }
}
