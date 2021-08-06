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
package com.taotao.cloud.bigdata.hudi

import com.taotao.cloud.bigdata.hudi.util.SparkHelper
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

/**
 * TaoTaoCloudLogConsole
 *
 * -e dev -b host:9092 -t taotao-cloud-sys-log -m 0
 *
 * spark-submit --master spark://172.16.6.151:7077 --class com.taotao.cloud.bigdata.hudi.TaoTaoCloudLogConsole  taotao-cloud-hudi-1.8.0.jar
 *
 * java -cp taotao-cloud-hudi-1.8.0.jar  com.taotao.cloud.bigdata.hudi.TaoTaoCloudLogConsole
 *
 * @author shuigedeng
 * @since 2020/11/27 下午3:06
 * @version 1.0.0
 */
object TaoTaoCloudLogConsole {
  val logger: Logger = LoggerFactory.getLogger(TaoTaoCloudLogConsole.getClass)

  def main(args: Array[String]): Unit = {
    System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
      "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl")

    System.setProperty("HADOOP_USER_NAME", "root")

    //    val config: AccessLogConf = AccessLogConf.parseConf(TaoTaoCloudLogConsole, args)
    //    val spark: SparkSession = SparkHelper.getSparkSession(config.env)
    val spark: SparkSession = SparkHelper.getSparkSession("dev")

    import spark.implicits._

    val dataframe: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "172.16.6.151:9092")
      .option("subscribe", "sys-log-taotao-cloud-gateway")
      //      .option("subscribe", "*-taotao-cloud-*")
      //      .option("subscribePattern", "sys-log-*")
      .option("staringOffsets", "earliest")
      .option("maxOffsetsPerTrigger", 100000)
      .option("failOnDataLoss", value = false)
      .option("kafka.consumer.commit.groupid", "AccessLogHudi")
      .option("group.id", "Log2Console")
      .load()

    dataframe
      .selectExpr("cast(value as string)", "cast(key as string)", "topic",
        "partition", "offset", "timestamp", "timestampType")
      .as[(String, String, String, Int, Long, Long, Int)]
      .writeStream
      .trigger(Trigger.ProcessingTime(3 + " seconds"))
      //      .queryName("action-log")
      .outputMode(OutputMode.Append())
      .format("console")
      .start()
      .awaitTermination()
  }
}
