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

import com.taotao.cloud.bigdata.hudi.util.{HudiUtil, MetaUtil, SparkHelper}
import org.apache.hudi.DataSourceWriteOptions
import org.apache.hudi.config.{HoodieIndexConfig, HoodieWriteConfig}
import org.apache.hudi.index.HoodieIndex
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.streaming.StreamingQueryListener.{QueryProgressEvent, QueryStartedEvent, QueryTerminatedEvent}
import org.apache.spark.sql.streaming.{StreamingQueryListener, Trigger}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable

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
* @since 2022 -07-21 10:46:00
*/
object QianFengLogHudi {
  val LOGGER: Logger = LoggerFactory.getLogger(QianFengLogHudi.getClass)

  def main(args: Array[String]): Unit = {
    val accessLogConf: config.AccessLogConf = config.AccessLogConf.parseConf(QianFengLogHudi, args)
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

    import org.apache.spark.sql.functions._
    import sparkSession.implicits._

    val dataframe: DataFrame = sparkSession
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", accessLogConf.brokerList)
      .option("subscribe", accessLogConf.sourceTopic)
      .option("staringOffsets", "earliest")
      .option("maxOffsetsPerTrigger", 100000)
      .option("failOnDataLoss", value = false)
      .option("kafka.consumer.commit.groupid", "AccessLogHudi")
      .load()

    val metaType: String = accessLogConf.metaType
    val meta_log_json_str: String = MetaUtil.getMetaJson(metaType.toInt)
    val log_meta: DataFrame = sparkSession.read.json(Seq(meta_log_json_str).toDS())
    val log_meta_schema: StructType = log_meta.schema

    val msb: Broadcast[StructType] = sparkSession.sparkContext.broadcast(log_meta_schema)

    dataframe
      .selectExpr("CAST(value as String)")
      .as[String]
      .writeStream
      .queryName("AccessLogHudi")
      .option("checkpointLocation", accessLogConf.checkpointDir + "/action/")
      .trigger(Trigger.ProcessingTime(accessLogConf.trigger + " seconds"))
      .foreachBatch((batchDF: Dataset[String], batchId: Long) => {
        batchDF.persist()

        val union: String => String = new extract.LogExtract().unionMetaAndBody(_)
        val newDF: Dataset[String] = batchDF.filter(_ != null).map(union)

        if (!newDF.isEmpty) {
          val hudiConf: mutable.Map[String, String] = HudiUtil
            .getHudiConf(
              accessLogConf.tableType,
              accessLogConf.syncJDBCUrl,
              accessLogConf.syncJDBCUsername
            )

          newDF
            .select(from_json($"value", msb.value).as("access_log"))
            .select("access_log.*")
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
        }

        batchDF.unpersist()
        println()
      })
      .start()
      .awaitTermination()

    //val action_data_log_json_str = MetaUtil.getMetaJson(config.metaType)
    //val sys_access_log_json_str = MetaUtil.getMetaJson(1)

    //val action_data_log_meta = spark.read.json(Seq(action_data_log_json_str).toDS())
    //val sys_access_log_meta = spark.read.json(Seq(sys_access_log_json_str).toDS())
    //val action_data_log_schema = action_data_log_meta.schema
    //val sys_access_log_schema = sys_access_log_meta.schema
    //
    //val msb = spark.sparkContext.broadcast((action_data_log_schema, sys_access_log_schema))

    //    dataframe.selectExpr("CAST(value as String)")
    //      .as[String]
    //      .writeStream
    //      .queryName("action2hudi")
    //      //.option("checkpointLocation", config.checkpointDir + "/action/")
    //      .trigger(Trigger.ProcessingTime(config.trigger + " seconds"))
    //      .foreachBatch((batchDF: Dataset[String], batchId: Long) => {
    //        batchDF.persist()
    //
    //        val newDF: Dataset[String] = batchDF.map(new NewsAction().unionMeatAndBody(_, action_data_log_json_str))
    //
    //        if (!newDF.isEmpty) {
    //          newDF.select(from_json($"value", msb.value._1).as("action_data"))
    //            .select("action_data.*")
    //            .show()
    //          .filter("type='event'")
    //          .write
    //                      .format("org.apache.hudi")
    //                      .options(HudiUtil.getEnentConfig(config.tableType, config.syncJDBCUrl, config.syncJDBCUsername))
    //                      .option(HoodieWriteConfig.TABLE_NAME, "event")
    //                      .option(DataSourceWriteOptions.HIVE_DATABASE_OPT_KEY, config.syncDB)
    //                      .option(HoodieIndexConfig.BLOOM_INDEX_UPDATE_PARTITION_PATH, "true")
    //                      .option(HoodieIndexConfig.INDEX_TYPE_PROP, HoodieIndex.IndexType.GLOBAL_BLOOM.name())
    //                      .mode(SaveMode.Append)
    //                      .save(config.hudiEventBasePath)
    //
    //                    newDF.select(from_json($"value", msb.value._2).as("data_user"))
    //                      .select("data_user.*")
    //                      .filter("type=user")
    //                      .write
    //                      .format("org.apache.hudi")
    //                      .options(HudiUtil.getUserConfig(config.tableType, config.syncJDBCUrl, config.syncJDBCUsername))
    //                      .option(HoodieWriteConfig.TABLE_NAME, "user")
    //                      .option(DataSourceWriteOptions.HIVE_DATABASE_OPT_KEY, config.syncDB)
    //                      .option(HoodieIndexConfig.BLOOM_INDEX_UPDATE_PARTITION_PATH, "true")
    //                      .option(HoodieIndexConfig.INDEX_TYPE_PROP, HoodieIndex.IndexType.GLOBAL_BLOOM.name())
    //                      .mode(S¬aveMode.Append)
    //                      .save(config.hudiUserBasePath)
    //        }
    //
    //        batchDF.unpersist()
    //        printf("----------------------")
    //      })
    //      .start()
    //      .awaitTermination()
  }
}
