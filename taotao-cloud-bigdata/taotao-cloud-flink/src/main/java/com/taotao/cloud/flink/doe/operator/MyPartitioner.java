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

package com.taotao.cloud.flink.doe.operator;

import org.apache.flink.api.common.functions.Partitioner;

/**
 * @since: 2023/12/31
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class MyPartitioner implements Partitioner<String> {

    /**
     * 传入一个key 根据key的值去计算  当前数据传递到下游的哪个分区
     * @param key The key.
     * @param numPartitions The number of partitions to partition into.
     * @return
     */
    @Override
    public int partition(String key, int numPartitions) {
        return (key.hashCode() & Integer.MAX_VALUE) % numPartitions;
    }
}
