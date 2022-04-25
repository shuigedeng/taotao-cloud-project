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
package com.taotao.cloud.bigdata.spark

import org.apache.spark.{SparkConf, SparkContext}

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
./bin/spark-submit \
  --class com.taotao.cloud.bigdata.spark.ScalaWordCount \
  --master spark://172.16.6.151:7077 \
  --deploy-mode client \
  --driver-memory 2g \
  --executor-memory 1g \
  --executor-cores 2 \
  --queue default \
  /opt/bigdata/spark-3.0.0-bin-hadoop3.2/jar/taotao-cloud-spark-2022.03.jar \
  /opt/spark/input /opt/spark/output
 * <p>
 *
 * @author shuigedeng
 * @since 2020/11/26 上午9:35
 * @version 2022.04
 */
object ScalaWordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("WordCountApp")
//      .setMaster("local[1]")

    val context = new SparkContext(conf)

    context.textFile(args(0))
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .sortBy(_._2, ascending = false)
      .saveAsTextFile(args(1))

    context.stop()
  }
}
