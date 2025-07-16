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

package com.taotao.cloud.hadoop.mr.component.provinceflow;

import java.util.HashMap;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * K2  V2  对应的是map输出kv的类型
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:29
 */
public class ProvincePartitioner extends Partitioner<Text, FlowBean> {

    public static HashMap<String, Integer> provinceDict = new HashMap<>();

    static {
        provinceDict.put("136", 0);
        provinceDict.put("137", 1);
        provinceDict.put("138", 2);
        provinceDict.put("139", 3);
    }

    @Override
    public int getPartition(Text key, FlowBean value, int numPartitions) {
        String prefix = key.toString().substring(0, 3);
        Integer provinceId = provinceDict.get(prefix);

        return provinceId == null ? 4 : provinceId;
    }
}
