package com.taotao.cloud.hudi

import com.taotao.cloud.bigdata.hudi.util.SparkHelper
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

/**
*
TaoTaoCloudLogHudi
*
*   taotao-cloud-hudi-1.0.jar -e dev -b taotao-cloud:9092 -t taotao-cloud-access-log -m 0 -i 10 -c /checkpoint/spark/taotao/cloud/access/log/hudi -g /taotao/cloud/access/log/cow/parquet -s taotao-cloud-acccess-log -y cow -r jdbc:hive2://192.168.1.5:10000 -n root
*
*   -e dev -b host:9092 -t taotao-cloud-backend -m 0 -i 10 -c /checkpoint/spark/log_hudi -g /user/hudi/cow/action_data -s action_data -y cow -r jdbc:hive2://192.68.99.37:10000 -n root
*
*   /opt/spark-3.0.0-bin-hadoop3.2/bin/spark-submit  taotao-cloud-hudi-1.0.jar -e dev -b taotao-cloud:9092 -t taotao-cloud-access-log -m 0 -i 10 -c /checkpoint/spark/taotao/cloud/access/log/hudi -g /taotao/cloud/access/log/cow/parquet -s taotao-cloud-acccess-log -y cow -r jdbc:hive2://192.168.1.5:10000 -n root
*
*
*
* @author shuigedeng
* @version 2022.10
* @since 2022 -07-21 10:45:42
*/
object TaoTaoCloudLogEs {
  val LOGGER: Logger = LoggerFactory.getLogger(TaoTaoCloudLogEs.getClass)

  def main(args: Array[String]): Unit = {
    val accessLogConf: config.AccessLogConf = config.AccessLogConf.parseConf(QianFengLogHudi, args)
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
