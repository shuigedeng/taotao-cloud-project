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

package com.taotao.cloud.hadoop.mr.component.flowsum;

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
 * 13480253104 180 180 360 13502468823 7335 110349 117684 13560436666 1116 954 2070
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:22
 */
public class FlowCountSort {

    /**
     * FlowCountSortMapper
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    static class FlowCountSortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

        FlowBean bean = new FlowBean();
        Text v = new Text();

        @Override
        protected void map( LongWritable key, Text value, Context context )
                throws IOException, InterruptedException {
            // 拿到的是上一个统计程序的输出结果，已经是各手机号的总流量信息
            String line = value.toString();
            String[] fields = line.split("\t");
            String phoneNbr = fields[0];

            long upFlow = Long.parseLong(fields[1]);
            long dFlow = Long.parseLong(fields[2]);

            bean.set(upFlow, dFlow);
            v.set(phoneNbr);

            context.write(bean, v);
        }
    }

    /**
     * 根据key来掉, 传过来的是对象, 每个对象都是不一样的, 所以每个对象都调用一次reduce方法
     *
     * @author shuigedeng
     * @version 2022.04
     * @since 2020/11/26 下午8:22
     */
    static class FlowCountSortReducer extends Reducer<FlowBean, Text, Text, FlowBean> {

        // <bean(),phonenbr>
        @Override
        protected void reduce( FlowBean bean, Iterable<Text> values, Context context )
                throws IOException, InterruptedException {
            context.write(values.iterator().next(), bean);
        }
    }

    public static void main( String[] args ) throws Exception {
        Configuration conf = new Configuration();
        /*conf.set("mapreduce.framework.name", "yarn");
        conf.set("yarn.resoucemanager.hostname", "mini1");*/
        Job job = Job.getInstance(conf);

        /*job.setJar("/home/hadoop/wc.jar");*/
        // 指定本程序的jar包所在的本地路径
        job.setJarByClass(FlowCountSort.class);

        // 指定本业务job要使用的mapper/Reducer业务类
        job.setMapperClass(FlowCountSortMapper.class);
        job.setReducerClass(FlowCountSortReducer.class);

        // 指定mapper输出数据的kv类型
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        // 指定最终输出的数据的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 指定job的输入原始文件所在目录
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        // 指定job的输出结果所在目录

        Path outPath = new Path(args[1]);
        /*FileSystem fs = FileSystem.get(conf);
        if(fs.exists(outPath)){
            fs.delete(outPath, true);
        }*/
        FileOutputFormat.setOutputPath(job, outPath);

        // 将job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn去运行
        /*job.submit();*/
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
