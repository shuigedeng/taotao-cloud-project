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

package com.taotao.cloud.mq.common.hash;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.common.hash.api.IHashCode;
import com.taotao.cloud.mq.common.hash.core.HasheCodes;
import com.xkzhangsan.time.utils.CollectionUtil;
import java.util.Collection;
import java.util.HashSet;

public final class ConsistentHashingBs<T> {
    private int virtualNum = 16;
    private IHashCode hashCode = HasheCodes.jdk();
    private Collection<T> nodes = new HashSet();

    public static <T> ConsistentHashingBs<T> newInstance() {
        return new ConsistentHashingBs<T>();
    }

    public ConsistentHashingBs<T> virtualNum(int virtualNum) {
        ArgUtils.gt("virtualNum", (long) virtualNum, 0L);
        this.virtualNum = virtualNum;
        return this;
    }

    public ConsistentHashingBs<T> hashCode(IHashCode hashCode) {
        ArgUtils.notNull(hashCode, "hashCode");
        this.hashCode = hashCode;
        return this;
    }

    public ConsistentHashingBs<T> nodes(Collection<T> nodes) {
        ArgUtils.notEmpty(nodes, "nodes");
        this.nodes = nodes;
        return this;
    }

    public IConsistentHashing<T> build() {
        IConsistentHashing<T> hashing = new ConsistentHashing(this.virtualNum, this.hashCode);
        if (CollectionUtil.isNotEmpty(this.nodes)) {
            for (T node : this.nodes) {
                hashing.add(node);
            }
        }

        return hashing;
    }
}
