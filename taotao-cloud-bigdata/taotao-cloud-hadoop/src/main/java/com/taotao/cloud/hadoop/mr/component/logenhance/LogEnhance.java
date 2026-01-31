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

package com.taotao.cloud.hadoop.mr.component.logenhance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * LogEnhance
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:26
 */
public class LogEnhance {

    /**
     * LogEnhanceMapper
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    static class LogEnhanceMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        Map<String, String> ruleMap = new HashMap<>();
        Text k = new Text();
        NullWritable v = NullWritable.get();

        // 从数据库中加载规则信息倒ruleMap中
        @Override
        protected void setup( Context context ) throws IOException, InterruptedException {
            try {
                DBLoader.dbLoader(ruleMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void map( LongWritable key, Text value, Context context )
                throws IOException, InterruptedException {
            // 获取一个计数器用来记录不合法的日志行数, 组名, 计数器名称
            Counter counter = context.getCounter("malformed", "malformedline");
            String line = value.toString();
            String[] fields = StringUtils.split(line, '\t');
            try {
                String url = fields[26];
                String contentTag = ruleMap.get(url);
                // 判断内容标签是否为空，如果为空，则只输出url到待爬清单；如果有值，则输出到增强日志
                if (contentTag == null) {
                    k.set(url + "\t" + "tocrawl" + "\n");
                    context.write(k, v);
                } else {
                    k.set(line + "\t" + contentTag + "\n");
                    context.write(k, v);
                }

            } catch (Exception exception) {
                counter.increment(1);
            }
        }
    }

    public static void main( String[] args ) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(LogEnhance.class);
        job.setMapperClass(LogEnhanceMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // 要控制不同的内容写往不同的目标路径，可以采用自定义outputformat的方法
        job.setOutputFormatClass(LogEnhanceOutputFormat.class);

        FileInputFormat.setInputPaths(job, new Path("D:/srcdata/webloginput/"));

        // 尽管我们用的是自定义outputformat，但是它是继承制fileoutputformat
        // 在fileoutputformat中，必须输出一个_success文件，所以在此还需要设置输出path
        FileOutputFormat.setOutputPath(job, new Path("D:/temp/output/"));

        // 不需要reducer
        job.setNumReduceTasks(0);

        job.waitForCompletion(true);
        System.exit(0);
    }
}
