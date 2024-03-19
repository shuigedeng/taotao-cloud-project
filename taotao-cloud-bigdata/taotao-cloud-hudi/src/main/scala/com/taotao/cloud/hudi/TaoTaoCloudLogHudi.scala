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
package com.taotao.cloud.hudi

import com.taotao.cloud.bigdata.hudi.util.{SparkHelper, TaoTaoCloudUtil}
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

/**
*
TaoTaoCloudLogConsole
*
*   -e dev -b host:9092 -t taotao-cloud-sys-log -m 0
*
*   spark-submit --master spark://192.168.10.200:7077 --class com.taotao.cloud.bigdata.hudi.TaoTaoCloudLogHudi  taotao-cloud-hudi-1.8.0.jar
*
*   java -cp taotao-cloud-hudi-1.8.0.jar  com.taotao.cloud.bigdata.hudi.TaoTaoCloudLogHudi
*
*
*
* @author shuigedeng
* @version 2022.10
* @since 2022 -07-21 10:45:53
*/
object TaoTaoCloudLogHudi {
  val logger: Logger = LoggerFactory.getLogger(TaoTaoCloudLogHudi.getClass)

  def main(args: Array[String]): Unit = {
    System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
      "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl")

    System.setProperty("HADOOP_USER_NAME", "root")

    //    val config: AccessLogConf = AccessLogConf.parseConf(TaoTaoCloudLogHudi, args)
    //    val spark: SparkSession = SparkHelper.getSparkSession(config.env)
    val spark: SparkSession = SparkHelper.getSparkSession("dev")

    import spark.implicits._

    val dataframe: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "192.168.10.200:9092")
      .option("subscribe", "sys-log-taotao-cloud-sys")
      //      .option("subscribe", "*-taotao-cloud-*")
      //      .option("subscribePattern", "sys-log-*")
      .option("staringOffsets", "earliest")
      .option("maxOffsetsPerTrigger", 100000)
      .option("failOnDataLoss", value = false)
      .option("kafka.consumer.commit.groupid", "TaoTaoCloudLogHudi")
      .option("group.id", "TaoTaoCloudLogHudi")
      .load()

    dataframe
      .selectExpr(
        "topic as kafka_topic",
        "CAST(partition AS STRING) kafka_partition",
        "cast(timestamp as String) kafka_timestamp",
        "CAST(offset AS STRING) kafka_offset",
        "CAST(key AS STRING) kafka_key",
        "CAST(value AS STRING) kafka_value",
        "current_timestamp() current_time",
      )
      .selectExpr(
        "kafka_topic",
        "concat(kafka_partition,'-',kafka_offset) kafka_partition_offset",
        "kafka_offset",
        "kafka_timestamp",
        "kafka_key",
        "kafka_value",
        "substr(current_time,1,10) partition_date"
      )
      .as[LogEevent]
      .writeStream
      .queryName("taotao-cloud-hudi")
      .option("checkpointLocation", "/tmp/hudi/checkpoint")
      .trigger(Trigger.ProcessingTime(30 + " seconds"))
      .foreachBatch((batchDF: Dataset[LogEevent], batchId: Long) => {
        batchDF.persist()

        val mysqlBinlogDF: Dataset[LogEevent] = batchDF.filter(_.kafka_topic.startsWith("mysql-binlog"))
        if (!mysqlBinlogDF.isEmpty) {

        }

        val accessLogDF: Dataset[LogEevent] = batchDF.filter(_.kafka_topic.startsWith("access-log"))
        if (!accessLogDF.isEmpty) {

        }

        val requestLogDS: Dataset[LogEevent] = batchDF.filter(_.kafka_topic.startsWith("request-log"))
        if (!requestLogDS.isEmpty) {
          var topic: String = requestLogDS
            .select("kafka_topic")
            .first()
            .getString(0)
          topic = topic.replaceAll("-", "_");

          val requestLogDF: DataFrame = requestLogDS
            .selectExpr("cast (kafka_value as string) as json")
            .select(from_json($"json", schema = TaoTaoCloudUtil.requestLogSchema).as("requestLog"))
            .select("requestLog.*")

          TaoTaoCloudUtil.requestLog(requestLogDF, topic)
        }

        val sysLogDS: Dataset[LogEevent] = batchDF.filter(_.kafka_topic.startsWith("sys-log"))
        if (!sysLogDS.isEmpty) {
          var topic: String = sysLogDS
            .select("kafka_topic")
            .first()
            .getString(0)
          topic = topic.replaceAll("-", "_");

          val sysLogDF: DataFrame = sysLogDS
            .selectExpr("cast (kafka_value as string) as json")
            .select(from_json($"json", schema = TaoTaoCloudUtil.sysLogSchema).as("sysLog"))
            .select("sysLog.*")
          TaoTaoCloudUtil.sysLog(sysLogDF, topic)
        }

        batchDF.unpersist()
        println()
      })
      .start()
      .awaitTermination()
  }
}


case class LogEevent(kafka_topic: String,
                     kafka_partition_offset: String,
                     kafka_offset: String,
                     kafka_timestamp: String,
                     kafka_key: String,
                     kafka_value: String,
                     partition_date: String,
                    )
