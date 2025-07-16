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

package com.taotao.cloud.hadoop.mr.component.invertindex;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * InvertIndexStepOne
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:24
 */
public class InvertIndexStepOne {

    static class InverIndexStepOneMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        Text k = new Text();
        IntWritable v = new IntWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(" ");
            FileSplit inputSplit = (FileSplit) context.getInputSplit();
            String fileName = inputSplit.getPath().getName();
            for (String word : words) {
                k.set(word + "--" + fileName);
                context.write(k, v);
            }
        }
    }

    static class InverIndexStepOneReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable value : values) {
                count += value.get();
            }
            context.write(key, new IntWritable(count));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(InvertIndexStepOne.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path("D:/srcdata/inverindexinput"));
        FileOutputFormat.setOutputPath(job, new Path("D:/temp/out"));
        // FileInputFormat.setInputPaths(job, new Path(args[0]));
        // FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(InverIndexStepOneMapper.class);
        job.setReducerClass(InverIndexStepOneReducer.class);

        job.waitForCompletion(true);
    }
}
