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

import com.taotao.cloud.bigdata.hudi.config.AccessLogConf
import com.taotao.cloud.bigdata.hudi.util.SparkHelper
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

/**
 * TaoTaoCloudLogHudi
 *
 * taotao-cloud-hudi-1.0.jar -e dev -b taotao-cloud:9092 -t taotao-cloud-access-log -m 0 -i 10 -c /checkpoint/spark/taotao/cloud/access/log/hudi -g /taotao/cloud/access/log/cow/parquet -s taotao-cloud-acccess-log -y cow -r jdbc:hive2://192.168.1.5:10000 -n root
 *
 * -e dev -b host:9092 -t taotao-cloud-backend -m 0 -i 10 -c /checkpoint/spark/log_hudi -g /user/hudi/cow/action_data -s action_data -y cow -r jdbc:hive2://192.68.99.37:10000 -n root
 *
 * /opt/spark-3.0.0-bin-hadoop3.2/bin/spark-submit  taotao-cloud-hudi-1.0.jar -e dev -b taotao-cloud:9092 -t taotao-cloud-access-log -m 0 -i 10 -c /checkpoint/spark/taotao/cloud/access/log/hudi -g /taotao/cloud/access/log/cow/parquet -s taotao-cloud-acccess-log -y cow -r jdbc:hive2://192.168.1.5:10000 -n root
 *
 * @author shuigedeng
 * @since 2020/11/27 下午3:06
 * @version 1.0.0
 */
object AccessLogEs {
  val LOGGER: Logger = LoggerFactory.getLogger("AccessLogHudi")

  def main(args: Array[String]): Unit = {
    val accessLogConf: AccessLogConf = AccessLogConf.parseConf(AccessLogHudi, args)
    val spark: SparkSession = SparkHelper.getSparkSession(accessLogConf.env);

    //    DataFrameReader读ES
    val dataFrameReaderEsOptions = Map(
      "es.nodes.wan.only" -> "true",
      "es.nodes" -> "29.29.29.29:10008,29.29.29.29:10009",
      "es.port" -> "9200",
      "es.read.field.as.array.include" -> "arr1, arr2"
    )
    val dataFrameReaderEsDf = spark
      .read
      .format("es")
      .options(options = dataFrameReaderEsOptions)
      .load("index1/info")
    dataFrameReaderEsDf.show()

    //    DataFrameWriter写ES
    val dataFrameWriterEsOptions = Map(
      "es.index.auto.create" -> "true",
      "es.nodes.wan.only" -> "true",
      "es.nodes" -> "29.29.29.29:10008,29.29.29.29:10009",
      "es.port" -> "9200",
      "es.mapping.id" -> "id"
    )

    val sourceDF = spark.table("hive_table")
    sourceDF
      .write
      .format("org.elasticsearch.spark.sql")
      .options(dataFrameWriterEsOptions)
      .mode(SaveMode.Append)
      .save("hive_table/docs")

    //    Structured Streaming - ES
    val options = Map(
      "es.index.auto.create" -> "true",
      "es.nodes.wan.only" -> "true",
      "es.nodes" -> "29.29.29.29:10008,29.29.29.29:10009",
      "es.port" -> "9200",
      "es.mapping.id" -> "zip_record_id"
    )
    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "a:9092,b:9092,c:9092")
      .option("subscribe", "test")
      .option("failOnDataLoss", "false")
      .load()
    df
      .writeStream
      .outputMode(OutputMode.Append())
      .format("es")
      .option("checkpointLocation", s"hdfs://hadoop:8020/checkpoint/test01")
      .options(options)
      .start("test_streaming/docs")
      .awaitTermination()
  }
}
