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
package com.taotao.cloud.bigdata.hudi.util

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
    "password" -> "n3nbhbn3ymb!"
  )


  def apply(): MysqlUtil = {
    new MysqlUtil(mysqlConfig);
  }
}
