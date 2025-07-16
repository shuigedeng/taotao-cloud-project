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

package com.taotao.cloud.hadoop.mr.component.secondarysort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * ItemIdPartitioner
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:33
 */
public class ItemIdPartitioner extends Partitioner<OrderBean, NullWritable> {

    @Override
    public int getPartition(OrderBean bean, NullWritable value, int numReduceTasks) {
        // 相同id的订单bean，会发往相同的partition
        // 而且，产生的分区数，是会跟用户设置的reduce task数保持一致
        return (bean.getItemid().hashCode() & Integer.MAX_VALUE) % numReduceTasks;
    }
}
