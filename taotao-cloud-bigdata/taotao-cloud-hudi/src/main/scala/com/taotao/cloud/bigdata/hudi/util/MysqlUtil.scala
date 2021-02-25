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
    "url" -> "jdbc:mysql://106.13.201.31:3306/taotao-cloud?autoReconnect=true",
    "username" -> "root",
    "password" -> "n3nbhbn3ymb!"
  )


  def apply(): MysqlUtil = {
    new MysqlUtil(mysqlConfig);
  }
}
