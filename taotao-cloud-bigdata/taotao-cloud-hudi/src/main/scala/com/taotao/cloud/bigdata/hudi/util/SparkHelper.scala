package com.taotao.cloud.bigdata.hudi.util

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkHelper {
  def getSparkSession(env: String): SparkSession ={
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
          .setMaster("local[1]")
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
