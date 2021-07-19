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
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.{Logger, LoggerFactory}

/**
 * TaoTaoCloudLogConsole
 *
 * -e dev -b host:9092 -t taotao-cloud-sys-log -m 0
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

    val config: AccessLogConf = AccessLogConf.parseConf(AccessLogConsole, args)
    val spark: SparkSession = SparkHelper.getSparkSession(config.env)

    logger.info(spark.version)

    import spark.implicits._

    val dataframe: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", config.brokerList)
      .option("subscribe", config.sourceTopic)
      .option("startingOffsets", "earliest")
      .option("group.id", "Log2Console")
      .load()

    dataframe
      .selectExpr("cast(value as string)", "offset")
      .as[(String, Long)]
      .writeStream
      .trigger(Trigger.ProcessingTime(config.trigger + " seconds"))
      .queryName("action-log")
      .outputMode(OutputMode.Append())
      .format("console")
      .start()
      .awaitTermination()
  }
}
