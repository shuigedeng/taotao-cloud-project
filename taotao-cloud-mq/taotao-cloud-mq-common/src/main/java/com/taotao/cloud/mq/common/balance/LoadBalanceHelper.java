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

import com.taotao.cloud.mq.common.balance.impl.LoadBalances;
import com.taotao.boot.common.support.hash.api.HashCode;

import java.util.List;

/**
 * LoadBalanceHelper
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public final class LoadBalanceHelper {

    private LoadBalanceHelper() {
    }

    public static <T extends Server> T random( List<T> servers ) {
        return (T)
                LoadBalanceBs.<T>newInstance()
                        .servers(servers)
                        .loadBalance(LoadBalances.random())
                        .select();
    }

    public static <T extends Server> T roundRobbin( List<T> servers ) {
        return (T)
                LoadBalanceBs.<T>newInstance()
                        .servers(servers)
                        .loadBalance(LoadBalances.roundRobbin())
                        .select();
    }

    public static <T extends Server> T weightRoundRobbin( List<T> servers ) {
        return (T)
                LoadBalanceBs.<T>newInstance()
                        .servers(servers)
                        .loadBalance(LoadBalances.weightRoundRobbin())
                        .select();
    }

    public static <T extends Server> T commonHash(
            List<T> servers, HashCode hash, String hashKey ) {
        return (T)
                LoadBalanceBs.<T>newInstance()
                        .servers(servers)
                        .hashKey(hashKey)
                        .loadBalance(LoadBalances.commonHash(hash))
                        .select();
    }

    public static <T extends Server> T consistentHash(
            List<T> servers, HashCode hash, String hashKey ) {
        return (T)
                LoadBalanceBs.<T>newInstance()
                        .servers(servers)
                        .hashKey(hashKey)
                        .loadBalance(LoadBalances.consistentHash(hash))
                        .select();
    }
}
