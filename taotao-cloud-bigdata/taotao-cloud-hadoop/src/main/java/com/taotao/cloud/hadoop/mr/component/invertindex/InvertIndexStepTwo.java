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
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * InvertIndexStepTwo
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:24
 */
public class InvertIndexStepTwo {

    /**
     * IndexStepTwoMapper
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class IndexStepTwoMapper extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map( LongWritable key, Text value, Context context )
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] files = line.split("--");
            context.write(new Text(files[0]), new Text(files[1]));
        }
    }

    /**
     * IndexStepTwoReducer
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class IndexStepTwoReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce( Text key, Iterable<Text> values, Context context )
                throws IOException, InterruptedException {
            StringBuilder sb = new StringBuilder();
            for (Text text : values) {
                sb.append(text.toString().replace("\t", "-->")).append("\t");
            }
            context.write(key, new Text(sb.toString()));
        }
    }

    public static void main( String[] args ) throws Exception {
        if (args.length < 1) {
            args = new String[]{"D:/temp/out/part-r-00000", "D:/temp/out2"};
        }

        Configuration config = new Configuration();
        Job job = Job.getInstance(config);

        job.setMapperClass(IndexStepTwoMapper.class);
        job.setReducerClass(IndexStepTwoReducer.class);
        //		job.setMapOutputKeyClass(Text.class);
        //		job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 1 : 0);
    }
}
