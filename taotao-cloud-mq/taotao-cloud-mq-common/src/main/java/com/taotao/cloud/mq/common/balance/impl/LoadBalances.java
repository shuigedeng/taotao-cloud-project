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

import com.taotao.cloud.mq.common.balance.ILoadBalance;
import com.taotao.cloud.mq.common.balance.IServer;
import com.taotao.cloud.mq.common.hash.api.IHashCode;

public final class LoadBalances {
    private LoadBalances() {}

    public static <T extends IServer> ILoadBalance<T> random() {
        return new LoadBalanceRandom();
    }

    public static <T extends IServer> ILoadBalance<T> roundRobbin() {
        return new LoadBalanceRoundRobbin();
    }

    public static <T extends IServer> ILoadBalance<T> weightRoundRobbin() {
        return new LoadBalanceWeightRoundRobbin();
    }

    public static <T extends IServer> ILoadBalance<T> commonHash(IHashCode hashCode) {
        return new LoadBalanceCommonHash(hashCode);
    }

    public static <T extends IServer> ILoadBalance<T> consistentHash(IHashCode hashCode) {
        return new LoadBalanceConsistentHash(hashCode);
    }
}
