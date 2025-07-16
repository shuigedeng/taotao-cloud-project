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

package com.taotao.cloud.spark;

import java.util.Arrays;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

/**
 * 1.本地运行 本地数据参数
 * <p>
 * ----	 /Users/shuigedeng/spark/input /Users/shuigedeng/spark/input
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
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/26 上午9:35
 */
public class JavaWordCount {

    public static void main(String[] args) throws InterruptedException {
        SparkConf javaWordCount = new SparkConf().setAppName("JavaWordCount");
        // .setMaster("local[1]");

        JavaSparkContext jsc = new JavaSparkContext(javaWordCount);

        JavaPairRDD<String, Integer> counts =
                jsc.textFile(args[0])
                        .flatMap(lines -> Arrays.asList(lines.split(" ")).iterator())
                        .mapToPair(
                                new PairFunction<String, String, Integer>() {
                                    @Override
                                    public Tuple2<String, Integer> call(String word)
                                            throws Exception {
                                        return Tuple2.apply(word, 1);
                                    }
                                })
                        .reduceByKey(Integer::sum);

        JavaPairRDD<String, Integer> sorts =
                counts.mapToPair(
                                new PairFunction<Tuple2<String, Integer>, String, Integer>() {
                                    @Override
                                    public Tuple2<String, Integer> call(
                                            Tuple2<String, Integer> tuple2) throws Exception {
                                        return Tuple2.apply(tuple2._1(), tuple2._2());
                                    }
                                })
                        .sortByKey(false);

        sorts.saveAsTextFile(args[1]);

        jsc.stop();
    }
}
