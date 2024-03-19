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

import com.alibaba.fastjson2.JSONObject

import java.sql.{Connection, ResultSet, Statement}
import java.util.UUID
import scala.collection.mutable

object MetaUtil {
  def getMetaJson(metaType: Int): String = {
    val conn: Connection = MysqlUtil().getMysqlConn
    val statement: Statement = conn.createStatement()
    val sql: String = "select field, field_type from `taotao-cloud-log-meta` where meta_type = " + metaType

    val resultSet = statement.executeQuery(sql)
    val jsonMeta = new JSONObject()
    while (resultSet.next()) {
      val field = resultSet.getString("field")
      val fieldType = resultSet.getString("field_type")

      genSimpleJsonMeta(jsonMeta, field.trim(), fieldType)
    }

    conn.close()
    jsonMeta.put("uuid", UUID.randomUUID().toString)
    jsonMeta.toJSONString
  }

  def getMeta: mutable.HashMap[String, String] = {
    val columnMetaMap: mutable.HashMap[String, String] = new mutable.HashMap[String, String]
    val conn: Connection = MysqlUtil().getMysqlConn
    val statement: Statement = conn.createStatement()
    val resultSet: ResultSet = statement.executeQuery("select field, field_type from `taotao-cloud-log-meta`")

    while (resultSet.next()) {
      val field = resultSet.getString("field")
      val fieldType = resultSet.getString("field_type")

      columnMetaMap += (field -> fieldType)
    }
    conn.close()
    columnMetaMap
  }

  def genSimpleJsonMeta(jsonObj: JSONObject, field: String, fieldType: String): AnyRef = {
    fieldType.toLowerCase match {
      case "string" => jsonObj.put(field, "")
      case "int" => jsonObj.put(field, 0)
      case "bigint" => jsonObj.put(field, 0L)
      case "double" => jsonObj.put(field, 0.1)
      case "boolean" => jsonObj.put(field, false)
      case "datetime" => jsonObj.put(field, null)
      case "array" => jsonObj.put(field, List())
    }
  }
}
