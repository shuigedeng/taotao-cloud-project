package com.taotao.cloud.bigdata.hadoop.atguigu.mapreduce.a9_outputformat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LogMapper extends Mapper<LongWritable, Text,Text, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // http://www.baidu.com
        //http://www.google.com
        // (http://www.google.com, NullWritable)
        // 不做任何处理
        context.write(value, NullWritable.get());
    }
}
