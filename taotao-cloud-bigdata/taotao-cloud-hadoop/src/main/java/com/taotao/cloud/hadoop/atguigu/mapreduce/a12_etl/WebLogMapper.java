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

package com.taotao.cloud.hadoop.atguigu.mapreduce.a12_etl;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WebLogMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        // 1 获取一行
        String line = value.toString();

        // 2 ETL
        boolean result = parseLog(line, context);

        if (!result) {
            return;
        }

        // 3 写出
        context.write(value, NullWritable.get());
    }

    private boolean parseLog(String line, Context context) {
        // 切割
        // 1.206.126.5 - - [19/Sep/2013:05:41:41 +0000] "-" 400 0 "-" "-"
        String[] fields = line.split(" ");

        // 2 判断一下日志的长度是否大于11
        if (fields.length > 11) {
            return true;
        } else {
            return false;
        }
    }
}
