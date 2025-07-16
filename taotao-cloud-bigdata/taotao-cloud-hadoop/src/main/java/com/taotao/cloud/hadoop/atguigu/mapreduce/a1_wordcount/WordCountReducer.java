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

package com.taotao.cloud.hadoop.atguigu.mapreduce.a1_wordcount;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * KEYIN, reduce阶段输入的key的类型：Text
 * VALUEIN,reduce阶段输入value类型：IntWritable
 * KEYOUT,reduce阶段输出的Key类型：Text
 * VALUEOUT,reduce阶段输出的value类型：IntWritable
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable outV = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {

        int sum = 0;
        // atguigu, (1,1)
        // 累加
        for (IntWritable value : values) {
            sum += value.get();
        }

        outV.set(sum);

        // 写出
        context.write(key, outV);
    }
}
