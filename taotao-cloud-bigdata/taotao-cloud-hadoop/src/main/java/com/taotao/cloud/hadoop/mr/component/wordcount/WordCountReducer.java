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

package com.taotao.cloud.hadoop.mr.component.wordcount;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * KEYIN, VALUEIN 对应  mapper输出的KEYOUT,VALUEOUT类型对应
 * <p>
 * KEYOUT, VALUEOUT 是自定义reduce逻辑处理结果的输出数据类型
 * <p>
 * KEYOUT是单词 VLAUEOUT是总次数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:08
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    /**
     * <angelababy,1><angelababy,1><angelababy,1><angelababy,1><angelababy,1>
     * <hello,1><hello,1><hello,1><hello,1><hello,1><hello,1> <banana,1><banana,1><banana,1><banana,1><banana,1><banana,1>
     * 入参key，是一组相同单词kv对的key
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int count = 0;

        /*Iterator<IntWritable> iterator = values.iterator();
        while(iterator.hasNext()){
            count += iterator.next().get();
        }*/

        for (IntWritable value : values) {
            count += value.get();
        }
        context.write(key, new IntWritable(count));
    }
}
