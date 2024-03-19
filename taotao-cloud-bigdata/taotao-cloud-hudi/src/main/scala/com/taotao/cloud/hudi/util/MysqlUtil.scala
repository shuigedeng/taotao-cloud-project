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

import java.sql.{Connection, DriverManager}
import scala.collection.mutable

class MysqlUtil private(val config: collection.mutable.Map[String, String]) {

  def getMysqlConn: Connection = {
    var conn: Connection = null
    try {
      Class.forName(config("driver"))
      conn = DriverManager.getConnection(config("url"), config("username"), config("password"))
    } catch {
      case e: Exception =>
        e.printStackTrace()

        try {
          conn.close()
        } catch {
          case e: Exception => {
            e.printStackTrace()
          }
        }
    }
    conn
  }
}

object MysqlUtil {

  val mysqlConfig: mutable.Map[String, String] = collection.mutable.Map(
    "driver" -> "com.mysql.cj.jdbc.Driver",
    "url" -> "jdbc:mysql://host:3306/taotao-cloud?autoReconnect=true",
    "username" -> "root",
    "password" -> "123456!"
  )


  def apply(): MysqlUtil = {
    new MysqlUtil(mysqlConfig);
  }
}
