package com.taotao.cloud.bigdata.hadoop.atguigu.mapreduce.a2_writable;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FlowDriver {

	private static String PATH = "/Users/shuigedeng/Downloads/尚硅谷大数据/尚硅谷-hadoop3资料/11_input";


	public static void main(String[] args)
		throws IOException, ClassNotFoundException, InterruptedException {

		// 1 获取job
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		// 2 设置jar
		job.setJarByClass(FlowDriver.class);

		// 3 关联mapper 和Reducer
		job.setMapperClass(FlowMapper.class);
		job.setReducerClass(FlowReducer.class);

		// 4 设置mapper 输出的key和value类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);

		// 5 设置最终数据输出的key和value类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);

		// 6 设置数据的输入路径和输出路径
		FileInputFormat.setInputPaths(job, new Path(PATH + "/inputflow"));
		FileOutputFormat.setOutputPath(job, new Path(PATH + "/outputflow"));

		// 7 提交job
		boolean result = job.waitForCompletion(true);
		System.exit(result ? 0 : 1);
	}
}
