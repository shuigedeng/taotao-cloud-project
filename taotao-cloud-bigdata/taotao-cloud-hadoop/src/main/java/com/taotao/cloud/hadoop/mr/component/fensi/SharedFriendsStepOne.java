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

package com.taotao.cloud.hadoop.mr.component.fensi;

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
 * SharedFriendsStepOne
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:17
 */
public class SharedFriendsStepOne {

    /**
     * SharedFriendsStepOneMapper
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    static class SharedFriendsStepOneMapper extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map( LongWritable key, Text value, Context context )
                throws IOException, InterruptedException {
            // A:B,C,D,F,E,O
            String line = value.toString();
            String[] personFriends = line.split(":");
            String person = personFriends[0];
            String friends = personFriends[1];

            for (String friend : friends.split(",")) {
                // 输出<好友，人>
                context.write(new Text(friend), new Text(person));
            }
        }
    }

    /**
     * SharedFriendsStepOneReducer
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    static class SharedFriendsStepOneReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce( Text friend, Iterable<Text> persons, Context context )
                throws IOException, InterruptedException {
            StringBuilder sb = new StringBuilder();
            for (Text person : persons) {
                sb.append(person).append(",");
            }
            context.write(friend, new Text(sb.toString()));
        }
    }

    public static void main( String[] args ) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(SharedFriendsStepOne.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(SharedFriendsStepOneMapper.class);
        job.setReducerClass(SharedFriendsStepOneReducer.class);

        FileInputFormat.setInputPaths(job, new Path("D:/srcdata/friends"));
        FileOutputFormat.setOutputPath(job, new Path("D:/temp/out"));

        job.waitForCompletion(true);
    }
}
