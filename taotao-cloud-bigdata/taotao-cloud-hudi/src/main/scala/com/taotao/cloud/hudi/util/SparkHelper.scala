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
package com.taotao.cloud.hudi.util

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkHelper {
  def getSparkSession(env: String): SparkSession = {
    env match {
      case "prod" =>
        val conf: SparkConf = new SparkConf()
          .setAppName("AccessLogHudiProd")
          .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
          .set("spark.sql.hive.metastore.version", "3.1.2")
          .set("spark.sql.cbo.enabled", "true")
          .set("spark.default.parallelism", "2")
          .set("spark.sql.shuffle.partitions", "2")
          .set("spark.hadoop.dfs.client.block.write.replace-datanode-on-failure.enabled", "true")
          .set("spark.hadoop.dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER")

        val session: SparkSession = SparkSession
          .builder()
          .config(conf)
          .enableHiveSupport()
          .getOrCreate()

        session

      case "dev" =>
        val conf: SparkConf = new SparkConf()
          .setAppName("AccessLogHudiDev")
          .setMaster("local[*]")
          .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
          .set("spark.sql.hive.metastore.version", "3.1.2")
          .set("spark.sql.hive.metastore.jars", "maven")
          .set("spark.sql.cbo.enabled", "true")
          .set("spark.hadoop.dfs.client.block.write.replace-datanode-on-failure.enabled", "true")
          .set("spark.hadoop.dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER")

        SparkSession
          .builder()
          .config(conf)
          .enableHiveSupport()
          .getOrCreate()

      case _ =>
        println("exit")
        System.exit(-1)
        null
    }
  }
}
