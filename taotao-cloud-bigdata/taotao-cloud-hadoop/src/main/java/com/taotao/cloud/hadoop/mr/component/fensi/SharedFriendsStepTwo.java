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
import java.util.Arrays;
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
 * SharedFriendsStepTwo
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:18
 */
public class SharedFriendsStepTwo {

    /**
     * SharedFriendsStepTwoMapper
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    static class SharedFriendsStepTwoMapper extends Mapper<LongWritable, Text, Text, Text> {

        // 拿到的数据是上一个步骤的输出结果
        // A I,K,C,B,G,F,H,O,D,
        // 友 人，人，人
        @Override
        protected void map( LongWritable key, Text value, Context context )
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] friendPersons = line.split("\t");

            String friend = friendPersons[0];
            String[] persons = friendPersons[1].split(",");

            Arrays.sort(persons);

            for (int i = 0; i < persons.length - 1; i++) {
                for (int j = i + 1; j < persons.length; j++) {
                    // 发出 <人-人，好友> ，这样，相同的“人-人”对的所有好友就会到同1个reduce中去
                    context.write(new Text(persons[i] + "-" + persons[j]), new Text(friend));
                }
            }
        }
    }

    /**
     * SharedFriendsStepTwoReducer
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    static class SharedFriendsStepTwoReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce( Text person_person, Iterable<Text> friends, Context context )
                throws IOException, InterruptedException {
            StringBuilder sb = new StringBuilder();

            for (Text friend : friends) {
                sb.append(friend).append(" ");
            }
            context.write(person_person, new Text(sb.toString()));
        }
    }

    public static void main( String[] args ) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(SharedFriendsStepTwo.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(SharedFriendsStepTwoMapper.class);
        job.setReducerClass(SharedFriendsStepTwoReducer.class);

        FileInputFormat.setInputPaths(job, new Path("D:/temp/out/part-r-00000"));
        FileOutputFormat.setOutputPath(job, new Path("D:/temp/out2"));

        job.waitForCompletion(true);
    }
}
