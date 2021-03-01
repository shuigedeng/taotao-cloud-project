package com.taotao.cloud.bigdata.hudi

import com.taotao.cloud.bigdata.hudi.config.AccessLogConf
import com.taotao.cloud.bigdata.hudi.util.{HudiUtil, SparkHelper}
import org.apache.hudi.DataSourceWriteOptions
import org.apache.hudi.config.{HoodieIndexConfig, HoodieWriteConfig}
import org.apache.hudi.index.HoodieIndex
import org.apache.spark.sql.streaming.StreamingQueryListener.{QueryProgressEvent, QueryStartedEvent, QueryTerminatedEvent}
import org.apache.spark.sql.streaming.{DataStreamReader, StreamingQueryListener, Trigger}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable

/**
 * TaoTaoCloudLogHudi
 *
 * taotao-cloud-hudi-1.0.jar -e dev -b taotao-cloud:9092 -t taotao-cloud-access-log -m 0 -i 10 -c /checkpoint/spark/taotao/cloud/access/log/hudi -g /taotao/cloud/access/log/cow/parquet -s taotao-cloud-acccess-log -y cow -r jdbc:hive2://192.168.1.5:10000 -n root
 *
 * -e dev -b 106.13.201.31:9092 -t taotao-cloud-backend -m 0 -i 10 -c /checkpoint/spark/log_hudi -g /user/hudi/cow/action_data -s action_data -y cow -r jdbc:hive2://192.68.99.37:10000 -n root
 *
 * /opt/spark-3.0.0-bin-hadoop3.2/bin/spark-submit  taotao-cloud-hudi-1.0.jar -e dev -b taotao-cloud:9092 -t taotao-cloud-access-log -m 0 -i 10 -c /checkpoint/spark/taotao/cloud/access/log/hudi -g /taotao/cloud/access/log/cow/parquet -s taotao-cloud-acccess-log -y cow -r jdbc:hive2://192.168.1.5:10000 -n root
 *
 * @author dengtao
 * @since 2020/11/27 下午3:06
 * @version 1.0.0
 */
object AccessLogHudiCopy {
  val LOGGER: Logger = LoggerFactory.getLogger("AccessLogHudi")

  def main(args: Array[String]): Unit = {
    val accessLogConf: AccessLogConf = AccessLogConf.parseConf(AccessLogHudi, args)
    val sparkSession: SparkSession = SparkHelper.getSparkSession(accessLogConf.env);


    sparkSession.streams.addListener(new StreamingQueryListener() {
      override def onQueryStarted(queryStarted: QueryStartedEvent): Unit = {
        LOGGER.info("Query started: " + queryStarted.id)
      }

      override def onQueryTerminated(queryTerminated: QueryTerminatedEvent): Unit = {
        LOGGER.info("Query terminated: " + queryTerminated.id)
      }

      override def onQueryProgress(queryProgress: QueryProgressEvent): Unit = {
        LOGGER.info("Query made progress: " + queryProgress.progress)
      }
    })

    val dataFrameReader: DataStreamReader = sparkSession
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", accessLogConf.brokerList)
      .option("subscribe", accessLogConf.sourceTopic)
      .option("staringOffsets", "earliest")
      .option("maxOffsetsPerTrigger", 100000)
      .option("failOnDataLoss", value = false)
      .option("kafka.consumer.commit.groupid", "AccessLogHudi")

    val df: DataFrame = dataFrameReader
      .load()
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

    df.writeStream
      .queryName("AccessLogHudi")
      .option("checkpointLocation", accessLogConf.checkpointDir + "/action/")
      .trigger(Trigger.ProcessingTime(accessLogConf.trigger + " seconds"))
      .foreachBatch((batchDF: DataFrame, batchId: Long) => {
        batchDF.persist()

        val hudiConf: mutable.Map[String, String] = HudiUtil
          .getHudiConf(
            accessLogConf.tableType,
            accessLogConf.syncJDBCUrl,
            accessLogConf.syncJDBCUsername
          )

        batchDF
          .write
          .format("org.apache.hudi")
          .options(hudiConf)
          .option(HoodieWriteConfig.TABLE_NAME, accessLogConf.sourceTopic)
          .option(DataSourceWriteOptions.HIVE_DATABASE_OPT_KEY, accessLogConf.syncDB)
          .option(DataSourceWriteOptions.HIVE_TABLE_OPT_KEY, accessLogConf.sourceTopic)
          .option(HoodieIndexConfig.BLOOM_INDEX_UPDATE_PARTITION_PATH, "true")
          .option(
            HoodieIndexConfig.INDEX_TYPE_PROP,
            HoodieIndex.IndexType.GLOBAL_BLOOM.name()
          )
          .mode(SaveMode.Append)
          .save(accessLogConf.hudiBasePath)

        batchDF.unpersist()
        println()
      })
      .start()
      .awaitTermination()
  }
}
