package com.taotao.cloud.bigdata.hadoop.atguigu.mapreduce.a1_wordcount;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 提交到集群\
 * hadoop jar wc.jar \
 * com.atguigu.mapreduce.wordcount.WordCountDriver  \
 * /user/atguigu/input\
 * /user/atguigu/output
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/7/16 09:56
 */
public class WordCountDriver {

	private static String PATH = "/Users/shuigedeng/Downloads/尚硅谷大数据/尚硅谷-hadoop3资料/11_input";

	public static void main(String[] args)
		throws IOException, ClassNotFoundException, InterruptedException {

		// 1 获取job
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		// 2 设置jar包路径
		job.setJarByClass(WordCountDriver.class);

		// 3 关联mapper和reducer
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);

		// 4 设置map输出的kv类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		// 5 设置最终输出的kV类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// 6 设置输入路径和输出路径
		FileInputFormat.setInputPaths(job, new Path(PATH + "/inputword"));
		FileOutputFormat.setOutputPath(job, new Path(PATH + "/outputword"));

		// 7 提交job
		boolean result = job.waitForCompletion(true);

		System.exit(result ? 0 : 1);
	}
}
