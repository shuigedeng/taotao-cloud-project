/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.bigdata.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 1.本地运行 本地数据参数
 * <p>
 * ----	 /Users/dengtao/spark/input /Users/dengtao/spark/input
 * <p>
 * 2.本地运行 hadoop数据参数
 * <p>
 * ----  hadoop://127.0.0.1:9000/spark/input hadoop://127.0.0.1:9000/spark/input
 * <p>
 * 3.上传jar包提交集群运行
 * <p>
 * ./spark-submit \ --master spark://127.0.0.1:7077 \ --class com.taotao.cloud.spark.JavaWordCount \
 * --executor-memory 512m \ --total-executor-cores 2 \ /root/spark/jar/taotao-cloud-spark-1.0-all.jar
 * \ hdfs://127.0.0.1/spark/wordcount/input \ hdfs://127.0.0.1/spark/wordcount/output
 * <p>
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/11/26 上午9:35
 */
public class JavaWordCount {

	public static void main(String[] args) throws InterruptedException {
		SparkConf javaWordCount = new SparkConf()
			.setAppName("JavaWordCount")
			.setMaster("local[1]");

		JavaSparkContext jsc = new JavaSparkContext(javaWordCount);

		JavaPairRDD<String, Integer> counts = jsc.textFile("/Users/dengtao/spark/input")
			.flatMap(lines -> Arrays.asList(lines.split(" ")).iterator())
			.mapToPair(new PairFunction<String, String, Integer>() {
				@Override
				public Tuple2<String, Integer> call(String word) throws Exception {
					return Tuple2.apply(word, 1);
				}
			})
			.reduceByKey(Integer::sum);

		JavaPairRDD<String, Integer> sorts = counts
			.mapToPair(new PairFunction<Tuple2<String, Integer>, String, Integer>() {
				@Override
				public Tuple2<String, Integer> call(Tuple2<String, Integer> tuple2)
					throws Exception {
					return Tuple2.apply(tuple2._1(), tuple2._2());

				}
			})
			.sortByKey(false);

		sorts.saveAsTextFile("/Users/dengtao/spark/output");

		Thread.sleep(Long.MAX_VALUE);

		jsc.stop();
	}
}
