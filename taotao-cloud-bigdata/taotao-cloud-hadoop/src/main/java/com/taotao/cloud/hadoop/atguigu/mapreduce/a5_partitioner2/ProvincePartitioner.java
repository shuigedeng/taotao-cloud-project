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

package com.taotao.cloud.hadoop.atguigu.mapreduce.a5_partitioner2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ProvincePartitioner extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
        // text 是手机号

        String phone = text.toString();

        String prePhone = phone.substring(0, 3);

        int partition;

        if ("136".equals(prePhone)) {
            partition = 0;
        } else if ("137".equals(prePhone)) {
            partition = 1;
        } else if ("138".equals(prePhone)) {
            partition = 2;
        } else if ("139".equals(prePhone)) {
            partition = 3;
        } else {
            partition = 4;
        }

        return partition;
    }
}
