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

package com.taotao.cloud.hadoop.mr.component.weblogwash;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * WeblogPreProcess
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:35
 */
public class WeblogPreProcess {

    static class WeblogPreProcessMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        Text k = new Text();
        NullWritable v = NullWritable.get();

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            WebLogBean webLogBean = WebLogParser.parser(line);
            // 可以插入一个静态资源过滤（.....）
            /*WebLogParser.filterStaticResource(webLogBean);*/
            if (!webLogBean.isValid()) {
                return;
            }
            k.set(webLogBean.toString());
            context.write(k, v);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(WeblogPreProcess.class);

        job.setMapperClass(WeblogPreProcessMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path("C:/wordcount/weblog/input"));
        FileOutputFormat.setOutputPath(job, new Path("C:/wordcount/weblog/output"));

        job.waitForCompletion(true);
    }
}
