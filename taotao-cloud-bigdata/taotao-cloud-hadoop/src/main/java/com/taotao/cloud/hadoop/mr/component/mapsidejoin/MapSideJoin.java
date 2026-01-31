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

package com.taotao.cloud.hadoop.mr.component.mapsidejoin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import com.taotao.boot.common.utils.lang.StringUtils;
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
 * MapSideJoin
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:28
 */
public class MapSideJoin {

    /**
     * MapSideJoinMapper
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    public static class MapSideJoinMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        // 用一个hashmap来加载保存产品信息表
        Map<String, String> pdInfoMap = new HashMap<>();
        Text k = new Text();

        /**
         * 通过阅读父类Mapper的源码，发现 setup方法是在maptask处理数据之前调用一次 可以用来做一些初始化工作
         */
        @Override
        protected void setup( Context context ) throws IOException, InterruptedException {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(new FileInputStream("pdts.txt")));
            String line;
            while (StringUtils.isNotEmpty(line = br.readLine())) {
                String[] fields = line.split(",");
                pdInfoMap.put(fields[0], fields[1]);
            }
            br.close();
        }

        // 由于已经持有完整的产品信息表，所以在map方法中就能实现join逻辑了
        @Override
        protected void map( LongWritable key, Text value, Context context )
                throws IOException, InterruptedException {
            String orderLine = value.toString();
            String[] fields = orderLine.split("\t");
            String pdName = pdInfoMap.get(fields[1]);
            k.set(orderLine + "\t" + pdName);
            context.write(k, NullWritable.get());
        }
    }

    public static void main( String[] args ) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(MapSideJoin.class);

        job.setMapperClass(MapSideJoinMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path("D:/srcdata/mapjoininput"));
        FileOutputFormat.setOutputPath(job, new Path("D:/temp/output"));

        // 指定需要缓存一个文件到所有的maptask运行节点工作目录
        /* job.addArchiveToClassPath(archive); */
        // 缓存jar包到task运行节点的classpath中
        /* job.addFileToClassPath(file); */
        // 缓存普通文件到task运行节点的classpath中
        /* job.addCacheArchive(uri); */
        // 缓存压缩包文件到task运行节点的工作目录
        /* job.addCacheFile(uri) */
        // 缓存普通文件到task运行节点的工作目录

        // 将产品表文件缓存到task工作节点的工作目录中去
        job.addCacheFile(new URI("file:/D:/srcdata/mapjoincache/pdts.txt"));

        // map端join的逻辑不需要reduce阶段，设置reducetask数量为0
        job.setNumReduceTasks(0);

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
