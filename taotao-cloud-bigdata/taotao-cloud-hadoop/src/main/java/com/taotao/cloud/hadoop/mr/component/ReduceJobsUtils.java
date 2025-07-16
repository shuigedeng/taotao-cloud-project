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

package com.taotao.cloud.hadoop.mr.component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/30 17:43
 */
@Component
public class ReduceJobsUtils {

    @Value("${hdfs.path}")
    private String path;

    private static String hdfsPath;

    /**
     * 获取HDFS配置信息
     *
     * @return org.apache.hadoop.conf.Configuration
     * @author shuigedeng
     * @since 2020/11/26 下午8:11
     */
    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsPath);
        configuration.set("mapred.job.tracker", hdfsPath);
        // 运行在yarn的集群模式
        // configuration.set("mapreduce.framework.name", "yarn");
        // 这个配置是让main方法寻找该机器的mr环境
        // configuration.set("yarn.resourcemanmager.hostname", "node1");
        return configuration;
    }

    /**
     * 分组统计、排序
     *
     * @param jobName
     * @param inputPath
     * @param outputPath
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public static void groupSort(String jobName, String inputPath, String outputPath)
            throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = getConfiguration();
        Job job = Job.getInstance(conf, jobName);
        // job.setJarByClass(GroupSort.class);
        //
        // // 设置reduce文件拆分个数
        // // job.setNumReduceTasks(3);
        // // 设置mapper信息
        // job.setMapperClass(GroupSort.GroupSortMapper.class);
        // job.setPartitionerClass(GroupSort.GroupSortPartitioner.class);
        // job.setGroupingComparatorClass(GroupSort.GroupSortComparator.class);
        // // 设置reduce信息
        // job.setReducerClass(GroupSort.GroupSortReduce.class);
        //
        // // 设置Mapper的输出
        // job.setMapOutputKeyClass(GroupSortModel.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 设置mapper和reduce的输出格式，如果相同则只需设置一个
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 指定输入文件的位置
        FileInputFormat.addInputPath(job, new Path(inputPath));
        // 指定输入文件的位置
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        // 运行
        job.waitForCompletion(true);
    }

    @PostConstruct
    public void getPath() {
        hdfsPath = this.path;
    }

    public static String getHdfsPath() {
        return hdfsPath;
    }
}
