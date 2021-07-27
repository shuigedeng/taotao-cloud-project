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
import org.apache.hudi.DataSourceWriteOptions
import org.apache.hudi.common.model.{HoodieTableType, WriteOperationType}
import org.apache.hudi.config.{HoodieIndexConfig, HoodieWriteConfig}
import org.apache.hudi.index.HoodieIndex
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

/**
 * TaoTaoCloudLogConsole
 *
 * -e dev -b host:9092 -t taotao-cloud-sys-log -m 0
 *
 * spark-submit --master spark://172.16.3.240:7077 --class com.taotao.cloud.bigdata.hudi.AccessLogConsole  taotao-cloud-hudi-1.8.0.jar
 *
 * java -cp taotao-cloud-hudi-1.8.0.jar  com.taotao.cloud.bigdata.hudi.AccessLogConsole
 *
 * @author shuigedeng
 * @since 2020/11/27 下午3:06
 * @version 1.0.0
 */
object AccessLogConsole {
  val logger: Logger = LoggerFactory.getLogger("TaoTaoCloudLogConsole")

  def main(args: Array[String]): Unit = {
    System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
      "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl")

    System.setProperty("HADOOP_USER_NAME", "root")

    //    val config: AccessLogConf = AccessLogConf.parseConf(AccessLogConsole, args)
    //    val spark: SparkSession = SparkHelper.getSparkSession(config.env)
    val spark: SparkSession = SparkHelper.getSparkSession("dev")

    import spark.implicits._

    val dataframe: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "172.16.3.240:9092")
      .option("subscribe", "sys-log-taotao-cloud-gateway")
      //      .option("subscribe", "*-taotao-cloud-*")
      //      .option("subscribePattern", "sys-log-*")
      .option("staringOffsets", "earliest")
      .option("maxOffsetsPerTrigger", 100000)
      .option("failOnDataLoss", value = false)
      .option("kafka.consumer.commit.groupid", "AccessLogHudi")
      .option("group.id", "Log2Console")
      .load()

    //        dataframe
    //          .selectExpr("cast(value as string)", "cast(key as string)", "topic",
    //            "partition", "offset", "timestamp", "timestampType")
    //          .as[(String, String, String, Int, Long, Long, Int)]
    //          .writeStream
    //          .trigger(Trigger.ProcessingTime(10 + " seconds"))
    //          .outputMode(OutputMode.Append())
    //          .format("es")
    //          .option("checkpointLocation", s"file:///Users/dengtao/logs/checkpoint")
    //          .options(dataFrameWriterEsOptions)
    //          .start("test_streaming/test")
    //          .awaitTermination()

    //    dataframe
    //      .selectExpr("cast(value as string)", "cast(key as string)", "topic",
    //        "partition", "offset", "timestamp", "timestampType")
    //      .as[(String, String, String, Int, Long, Long, Int)]
    //      .writeStream
    //      .trigger(Trigger.ProcessingTime(3 + " seconds"))
    //      //      .queryName("action-log")
    //      .outputMode(OutputMode.Append())
    //      .format("console")
    //      .start()
    //      .awaitTermination()


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

        val requestLogDF: Dataset[LogEevent] = batchDF.filter(_.kafka_topic.startsWith("request-log"))
        if (!requestLogDF.isEmpty) {
          val topic: String = requestLogDF
            .select("kafka_topic")
            .first()
            .getString(0)

          val dataFrameWriterEsOptions = Map(
            "es.index.auto.create" -> "true",
            "es.nodes.wan.only" -> "true",
            "es.nodes" -> "172.16.3.240",
            "es.port" -> "9200",
            "es.write.operation" -> "upsert",
            "es.mapping.id" -> "id"
          )

          val schema: StructType = StructType(Seq(
            StructField("ctime", StringType, nullable = true),
            StructField("source", StringType, nullable = true),
            StructField("request_version", StringType, nullable = true),
            StructField("tenant_id", StringType, nullable = true),
            StructField("trace_id", StringType, nullable = true),
            StructField("skywalking_trace_id", StringType, nullable = true),
            StructField("zipkin_span_export", StringType, nullable = true),
            StructField("zipkin_trace_id", StringType, nullable = true),
            StructField("zipkin_span_id", StringType, nullable = true),
            StructField("zipkin_parent_span_id", StringType, nullable = true),
            StructField("stack_trace", StringType, nullable = true),
            StructField("host", StringType, nullable = true),
            StructField("message", StringType, nullable = true),
            StructField("line", StringType, nullable = true),
            StructField("method", StringType, nullable = true),
            StructField("file", StringType, nullable = true),
            StructField("level", StringType, nullable = true),
            StructField("logger", StringType, nullable = true),
            StructField("pid", StringType, nullable = true),
            StructField("thread", StringType, nullable = true),
            StructField("logday", StringType, nullable = true),
            StructField("timestamp", StringType, nullable = true),
            StructField("version", StringType, nullable = true),
            StructField("env", StringType, nullable = true),
            StructField("server_port", StringType, nullable = true),
            StructField("server_ip", StringType, nullable = true),
            StructField("app_name", StringType, nullable = true),
          ))

          requestLogDF
            .selectExpr("cast (kafka_value as string) as json")
            .select(from_json($"json", schema = schema).as("requestLog"))
            .select("requestLog.*")
            .write
            .format("es")
            .options(dataFrameWriterEsOptions)
            .mode(SaveMode.Append)
            .save(topic)
        }

        val sysLogDF: Dataset[LogEevent] = batchDF.filter(_.kafka_topic.startsWith("sys-log"))
        if (!sysLogDF.isEmpty) {

          val schema: StructType = StructType(Seq(
            StructField("ctime", StringType, nullable = true),
            StructField("source", StringType, nullable = true),
            StructField("request_version", StringType, nullable = true),
            StructField("tenant_id", StringType, nullable = true),
            StructField("trace_id", StringType, nullable = true),
            StructField("skywalking_trace_id", StringType, nullable = true),
            StructField("zipkin_span_export", StringType, nullable = true),
            StructField("zipkin_trace_id", StringType, nullable = true),
            StructField("zipkin_span_id", StringType, nullable = true),
            StructField("zipkin_parent_span_id", StringType, nullable = true),
            StructField("stack_trace", StringType, nullable = true),
            StructField("host", StringType, nullable = true),
            StructField("message", StringType, nullable = true),
            StructField("line", StringType, nullable = true),
            StructField("method", StringType, nullable = true),
            StructField("file", StringType, nullable = true),
            StructField("level", StringType, nullable = true),
            StructField("logger", StringType, nullable = true),
            StructField("pid", StringType, nullable = true),
            StructField("thread", StringType, nullable = true),
            StructField("logday", StringType, nullable = true),
            StructField("timestamp", StringType, nullable = true),
            StructField("version", StringType, nullable = true),
            StructField("env", StringType, nullable = true),
            StructField("server_port", StringType, nullable = true),
            StructField("server_ip", StringType, nullable = true),
            StructField("app_name", StringType, nullable = true),
          ))

          val topic: String = sysLogDF
            .select("kafka_topic")
            .first()
            .getString(0)

          sysLogDF
            .selectExpr("cast (kafka_value as string) as json")
            .select(from_json($"json", schema = schema).as("sysLog"))
            .select("sysLog.*")
            .write
            .format("org.apache.hudi")
//            .option(HoodieCompactionConfig.COMMITS_ARCHIVAL_BATCH_SIZE_PROP, "2")
//            .option(HoodieCompactionConfig.MIN_COMMITS_TO_KEEP_PROP, "3")
//            .option(HoodieCompactionConfig.MAX_COMMITS_TO_KEEP_PROP, "4")
//          小于该大小的文件均被视为小文件
//            .option(HoodieCompactionConfig.PARQUET_SMALL_FILE_LIMIT_BYTES, "0")

            .option(DataSourceWriteOptions.TABLE_TYPE_OPT_KEY, HoodieTableType.COPY_ON_WRITE.name)
            .option(DataSourceWriteOptions.OPERATION_OPT_KEY, WriteOperationType.INSERT.value)
            // 指定更新时间 数值大的会覆盖小的
            .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY, "kafka_timestamp")
            // 指定唯一id的列名 必须唯一并且不能为null
            .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY, "kafka_partition_offset")
            // 指定分区列
            .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY, "partition_date")
            // 当分区变更的时候 数据的分区目录是否变更
            .option(HoodieIndexConfig.BLOOM_INDEX_UPDATE_PARTITION_PATH, "true")
            // 设置索引类型
            .option(HoodieIndexConfig.INDEX_TYPE_PROP, HoodieIndex.IndexType.GLOBAL_BLOOM.name())

            //clustering配置
//            .option(HoodieClusteringConfig.INLINE_CLUSTERING_PROP, "true")
//            .option(HoodieClusteringConfig.INLINE_CLUSTERING_MAX_COMMIT_PROP, "4")
//            .option(HoodieClusteringConfig.CLUSTERING_TARGET_FILE_MAX_BYTES, HoodieClusteringConfig.DEFAULT_CLUSTERING_TARGET_FILE_MAX_BYTES)
//            .option(HoodieClusteringConfig.CLUSTERING_PLAN_SMALL_FILE_LIMIT, HoodieClusteringConfig.DEFAULT_CLUSTERING_PLAN_SMALL_FILE_LIMIT)
//            .option(HoodieClusteringConfig.CLUSTERING_SORT_COLUMNS_PROPERTY, "")

            //hive配置
            .option(HoodieWriteConfig.INSERT_PARALLELISM, "1500")
            .option(HoodieWriteConfig.UPSERT_PARALLELISM, "500")
            .option(HoodieWriteConfig.EMBEDDED_TIMELINE_SERVER_ENABLED, "false")
            .option(HoodieWriteConfig.TABLE_NAME, "ods_" + topic)
            .option(DataSourceWriteOptions.HIVE_STYLE_PARTITIONING_OPT_KEY, "true")
            .option(DataSourceWriteOptions.HIVE_DATABASE_OPT_KEY, "taotao-cloud-log")
            .option(DataSourceWriteOptions.HIVE_TABLE_OPT_KEY, "ods_" + topic)
            .option(DataSourceWriteOptions.HIVE_SYNC_ENABLED_OPT_KEY, "true")
            .option(DataSourceWriteOptions.HIVE_PARTITION_FIELDS_OPT_KEY, "logday")
            .option(DataSourceWriteOptions.HIVE_PARTITION_EXTRACTOR_CLASS_OPT_KEY, "org.apache.hudi.hive.MultiPartKeysValueExtractor")
            .option(DataSourceWriteOptions.HIVE_URL_OPT_KEY, "jdbc:hive2://172.16.3.240:10000")
            .option(DataSourceWriteOptions.HIVE_USER_OPT_KEY, "root")
            .option(DataSourceWriteOptions.HIVE_PASS_OPT_KEY, "root")

            //清理配置
//            .option(HoodieCompactionConfig.CLEANER_POLICY_PROP, "KEEP_LATEST_FILE_VERSIONS")
//            .option(HoodieCompactionConfig.CLEANER_FILE_VERSIONS_RETAINED_PROP, "")
//            .option(HoodieCompactionConfig.CLEANER_COMMITS_RETAINED_PROP, "2")
//            .option(HoodieCompactionConfig.AUTO_CLEAN_PROP, "true")
//            .option(HoodieCompactionConfig.ASYNC_CLEAN_PROP, "true")

            .mode(SaveMode.Append)
            .save("/user/hive/warehouse/taotao-cloud-log/ods_" + topic)
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
