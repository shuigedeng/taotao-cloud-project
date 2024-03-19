package com.taotao.cloud.hudi.util

import org.apache.hudi.DataSourceWriteOptions
import org.apache.hudi.common.model.{HoodieTableType, WriteOperationType}
import org.apache.hudi.config.{HoodieIndexConfig, HoodieWriteConfig}
import org.apache.hudi.index.HoodieIndex
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SaveMode}


object TaoTaoCloudUtil {
  val dataFrameWriterEsOptions = Map(
    "es.index.auto.create" -> "true",
    "es.nodes.wan.only" -> "true",
    "es.nodes" -> "192.168.10.200",
    "es.port" -> "9200",
    "es.write.operation" -> "upsert",
    "es.mapping.id" -> "id"
  )

  val sysLogSchema: StructType = StructType(Seq(
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

  def sysLog(sysLogDF: DataFrame, topic: String): Unit = {
    sysLogDF
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
      .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY, "ctime")
      // 指定唯一id的列名 必须唯一并且不能为null
      .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY, "ctime")
      // 指定分区列
      .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY, "logday")
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
      .option(DataSourceWriteOptions.HIVE_DATABASE_OPT_KEY, "taotao_cloud_log")
      .option(DataSourceWriteOptions.HIVE_TABLE_OPT_KEY, "ods_" + topic)
      .option(DataSourceWriteOptions.HIVE_SYNC_ENABLED_OPT_KEY, "true")
      .option(DataSourceWriteOptions.HIVE_PARTITION_FIELDS_OPT_KEY, "logday")
      .option(DataSourceWriteOptions.HIVE_PARTITION_EXTRACTOR_CLASS_OPT_KEY, "org.apache.hudi.hive.MultiPartKeysValueExtractor")
      .option(DataSourceWriteOptions.HIVE_URL_OPT_KEY, "jdbc:hive2://192.168.10.200:10000")
      .option(DataSourceWriteOptions.HIVE_USER_OPT_KEY, "root")
      .option(DataSourceWriteOptions.HIVE_PASS_OPT_KEY, "root")

      //清理配置
      //            .option(HoodieCompactionConfig.CLEANER_POLICY_PROP, "KEEP_LATEST_FILE_VERSIONS")
      //            .option(HoodieCompactionConfig.CLEANER_FILE_VERSIONS_RETAINED_PROP, "")
      //            .option(HoodieCompactionConfig.CLEANER_COMMITS_RETAINED_PROP, "2")
      //            .option(HoodieCompactionConfig.AUTO_CLEAN_PROP, "true")
      //            .option(HoodieCompactionConfig.ASYNC_CLEAN_PROP, "true")

      .mode(SaveMode.Append)
      .save("/user/hive/warehouse/taotao_cloud_log.db/ods_" + topic)

    sysLogDF
      .write
      .format("es")
      .options(dataFrameWriterEsOptions)
      .mode(SaveMode.Append)
      .save(topic)
    ()
  }



  def accessLog(): Unit = {

  }

  val requestLogSchema: StructType = StructType(Seq(
    StructField("trace_id", StringType, nullable = true),
    StructField("application_name", StringType, nullable = true),
    StructField("username", StringType, nullable = true),
    StructField("user_id", StringType, nullable = true),
    StructField("client_id", StringType, nullable = true),
    StructField("description", StringType, nullable = true),
    StructField("request_ip", StringType, nullable = true),
    StructField("operate_type", StringType, nullable = true),
    StructField("request_type", StringType, nullable = true),
    StructField("request_method_name", StringType, nullable = true),
    StructField("request_method", StringType, nullable = true),
    StructField("request_url", StringType, nullable = true),
    StructField("request_args", StringType, nullable = true),
    StructField("request_params", StringType, nullable = true),
    StructField("request_headers", StringType, nullable = true),
    StructField("request_ua", StringType, nullable = true),
    StructField("classpath", StringType, nullable = true),
    StructField("request_start_time", StringType, nullable = true),
    StructField("request_end_time", StringType, nullable = true),
    StructField("request_consuming_time", StringType, nullable = true),
    StructField("ex_detail", StringType, nullable = true),
    StructField("ex_desc", StringType, nullable = true),
    StructField("tenant_id", StringType, nullable = true),
    StructField("source", StringType, nullable = true),
    StructField("ctime", StringType, nullable = true),
    StructField("logday", StringType, nullable = true),
  ))

  def requestLog(requestLogDF: DataFrame, topic: String): Unit = {
    requestLogDF
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
      .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY, "ctime")
      // 指定唯一id的列名 必须唯一并且不能为null
      .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY, "ctime")
      // 指定分区列
      .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY, "logday")
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
      .option(DataSourceWriteOptions.HIVE_DATABASE_OPT_KEY, "taotao_cloud_log")
      .option(DataSourceWriteOptions.HIVE_TABLE_OPT_KEY, "ods_" + topic)
      .option(DataSourceWriteOptions.HIVE_SYNC_ENABLED_OPT_KEY, "true")
      .option(DataSourceWriteOptions.HIVE_PARTITION_FIELDS_OPT_KEY, "logday")
      .option(DataSourceWriteOptions.HIVE_PARTITION_EXTRACTOR_CLASS_OPT_KEY, "org.apache.hudi.hive.MultiPartKeysValueExtractor")
      .option(DataSourceWriteOptions.HIVE_URL_OPT_KEY, "jdbc:hive2://192.168.10.200:10000")
      .option(DataSourceWriteOptions.HIVE_USER_OPT_KEY, "root")
      .option(DataSourceWriteOptions.HIVE_PASS_OPT_KEY, "root")

      //清理配置
      //            .option(HoodieCompactionConfig.CLEANER_POLICY_PROP, "KEEP_LATEST_FILE_VERSIONS")
      //            .option(HoodieCompactionConfig.CLEANER_FILE_VERSIONS_RETAINED_PROP, "")
      //            .option(HoodieCompactionConfig.CLEANER_COMMITS_RETAINED_PROP, "2")
      //            .option(HoodieCompactionConfig.AUTO_CLEAN_PROP, "true")
      //            .option(HoodieCompactionConfig.ASYNC_CLEAN_PROP, "true")

      .mode(SaveMode.Append)
      .save("/user/hive/warehouse/taotao_cloud_log.db/ods_" + topic)

    requestLogDF
      .write
      .format("es")
      .options(dataFrameWriterEsOptions)
      .mode(SaveMode.Append)
      .save(topic)
  }

  def mysqlBinLog(): Unit = {

  }
}
