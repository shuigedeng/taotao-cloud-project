package com.taotao.cloud.bigdata.hudi.util

import scala.collection.mutable

object HudiUtil {

  def getHudiConf(tableType: String, hiveJDBCUrl: String, hiveJDBCUsername: String): mutable.HashMap[String, String] = {
    val props: mutable.HashMap[String, String] = new mutable.HashMap[String, String]
    tableType.toUpperCase() match {
      case "COW" =>
        props.put("hoodie.datasource.write.table.type", "COPY_ON_WRITE")
      case "MOR" =>
        props.put("hoodie.datasource.write.table.type", "MERGE_ON_READ")
        props.put("hoodie.compact.inline", "true")
        props.put("hoodie.compact.inline.max.delta.commits", "5")
      case _ =>
        props.put("hoodie.datasource.write.table.type", "COPY_ON_WRITE")
    }

    props.put("hoodie.datasource.write.operation", "insert")
    props.put("hoodie.datasource.write.recordKey.field", "uuid")
    props.put("hoodie.datasource.write.precombine.field", "uuid")
    props.put("hoodie.datasource.write.partitionpath.field", "logday")

    props.put("hoodie.commits.archival.batch", "2")
    props.put("hoodie.cleaner.commits.retained", "2")
    props.put("hoodie.keep.min.commits", "3")
    props.put("hoodie.keep.max.commits", "4")

    props.put("hoodie.insert.shuffle.parallelism", "1")
    props.put("hoodie.update.shuffle.parallelism", "1")

    props.put("hoodie.datasource.hive_sync.enable", "true")
    props.put("hoodie.datasource.hive_sync.partition_fields", "logday")
    props.put("hoodie.datasource.hive_sync.partition_extractor_class", "org.apache.hudi.hive.MultiPartKeysValueExtractor")
    props.put("hoodie.datasource.hive_sync.jdbcurl", hiveJDBCUrl)
    props.put("hoodie.datasource.hive_sync.username", hiveJDBCUsername)

    props
  }

  def getUserConfig(tableType: String, hiveJDBCUrl: String, hiveJDBCUsername: String): mutable.HashMap[String, String] = {
    val props: mutable.HashMap[String, String] = new mutable.HashMap[String, String]
    tableType.toUpperCase() match {
      case "COW" =>
        props.put("hoodie.datasource.write.table.type", "COPY_ON_WRITE")
      case "MOR" =>
        props.put("hoodie.datasource.write.table.type", "MERGE_ON_READ")
        props.put("hoodie.compact.inline", "true")
        props.put("hoodie.compact.inline.max.delta.commits", "5")
      case _ =>
        props.put("hoodie.datasource.write.table.type", "COPY_ON_WRITE")
    }

    props.put("hoodie.datasource.write.keygenerator.class", "org.apache.hudi.keygen.NonPartitionedKeyGenerator")
    props.put("hoodie.datasource.hive_sync.partition_extractor_class", "org.apache.hudi.hive.NonPartitionedExtractor")

    props.put("hoodie.datasource.write.operation", "upsert")
    props.put("hoodie.datasource.write.recordKey.field", "distinct_id")
    props.put("hoodie.datasource.write.precombine.field", "uuid")

    props.put("hoodie.commits.archival.batch", "2")
    props.put("hoodie.cleaner.commits.retained", "2")
    props.put("hoodie.keep.min.commits", "3")
    props.put("hoodie.keep.max.commits", "4")

    props.put("hoodie.insert.shuffle.parallelism", "1")
    props.put("hoodie.update.shuffle.parallelism", "1")

    props.put("hoodie.datasource.hive_sync.database", "default")
    props.put("hoodie.datasource.hive_sync.table", "event")
    props.put("hoodie.datasource.hive_sync.enable", "true")
    props.put("hoodie.datasource.hive_sync.jdbcurl", hiveJDBCUrl)
    props.put("hoodie.datasource.hive_sync.username", hiveJDBCUsername)

    props
  }

}
