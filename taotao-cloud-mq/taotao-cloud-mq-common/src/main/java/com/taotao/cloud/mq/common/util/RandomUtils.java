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

package com.taotao.cloud.mq.common.util;

import com.taotao.cloud.mq.common.balance.LoadBalance;
import com.taotao.cloud.mq.common.balance.Server;
import com.taotao.cloud.mq.common.balance.impl.LoadBalanceContext;
import com.xkzhangsan.time.utils.CollectionUtil;
import com.xkzhangsan.time.utils.StringUtil;
import java.util.List;
import java.util.Objects;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class RandomUtils {

    /**
     * 负载均衡
     *
     * @param list 列表
     * @param key  分片键
     * @return 结果
     * @since 2024.05
     */
    public static <T extends Server> T loadBalance(
            final LoadBalance<T> loadBalance, final List<T> list, String key) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }

        if (StringUtil.isEmpty(key)) {
            LoadBalanceContext<T> loadBalanceContext =
                    LoadBalanceContext.<T>newInstance().servers(list);
            return loadBalance.select(loadBalanceContext);
        }

        // 获取 code
        int hashCode = Objects.hash(key);
        int index = hashCode % list.size();
        return list.get(index);
    }
}
