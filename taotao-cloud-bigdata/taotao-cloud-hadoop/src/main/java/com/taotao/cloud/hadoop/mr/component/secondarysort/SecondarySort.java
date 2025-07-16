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

package com.taotao.cloud.hadoop.mr.component.secondarysort;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * SecondarySort
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 下午8:34
 */
public class SecondarySort {

    static class SecondarySortMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {

        OrderBean bean = new OrderBean();

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = StringUtils.split(line, ",");

            bean.set(new Text(fields[0]), new DoubleWritable(Double.parseDouble(fields[2])));
            context.write(bean, NullWritable.get());
        }
    }

    static class SecondarySortReducer
            extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {

        // 到达reduce时，相同id的所有bean已经被看成一组，且金额最大的那个一排在第一位
        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context)
                throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(SecondarySort.class);

        job.setMapperClass(SecondarySortMapper.class);
        job.setReducerClass(SecondarySortReducer.class);

        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path("c:/wordcount/gpinput"));
        FileOutputFormat.setOutputPath(job, new Path("c:/wordcount/gpoutput"));

        // 在此设置自定义的Groupingcomparator类
        job.setGroupingComparatorClass(ItemidGroupingComparator.class);

        // 在此设置自定义的partitioner类
        job.setPartitionerClass(ItemIdPartitioner.class);

        job.setNumReduceTasks(2);

        job.waitForCompletion(true);
    }
}
