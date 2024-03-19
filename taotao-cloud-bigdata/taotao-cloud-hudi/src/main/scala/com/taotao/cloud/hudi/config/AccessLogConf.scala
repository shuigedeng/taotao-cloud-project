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
package com.taotao.cloud.hudi.config

import scopt.OptionParser

case class AccessLogConf(
                          var env: String = "",
                          var brokerList: String = "",
                          var sourceTopic: String = "",
                          var metaType: String = "",
                          var checkpointDir: String = "",
                          var path: String = "",
                          var trigger: String = "50",
                          var hudiBasePath: String = "",
                          var tableType: String = "COW",
                          var syncDB: String = "",
                          var syncJDBCUrl: String = "",
                          var syncJDBCUsername: String = ""
                        )

object AccessLogConf {
  def parseConf(obj: Object, args: Array[String]): AccessLogConf = {
    val programName: String = obj.getClass.getSimpleName.replaceAll("\\$", "")
    val parser: OptionParser[AccessLogConf] = new OptionParser[AccessLogConf]("programName") {
      head("programName", "3.x")
      opt[String]('e', "env").required().action((x, config) => config.copy(env = x)).text("env dev or prod")
      opt[String]('b', "brokerList").required().action((x, config) => config.copy(brokerList = x)).text("brokerList")
      opt[String]('t', "sourceTopic").required().action((x, config) => config.copy(sourceTopic = x)).text("sourceTopic")
      opt[String]('m', "metaType").required().action((x, config) => config.copy(metaType = x)).text("metaType")

      programName match {
        case "AccessLogConsole" =>

        case "AccessLog2Hdfs" =>
          opt[String]('c', "checkpointDir").required().action((x, config) => config.copy(checkpointDir = x)).text("checkpointDir")
          opt[String]('p', "path").required().action((x, config) => config.copy(path = x)).text("path")
          opt[String]('i', "trigger").required().action((x, config) => config.copy(trigger = x)).text("trigger")

        case "AccessLogHudi" =>
          opt[String]('i', "trigger").required().action((x, config) => config.copy(trigger = x)).text("trigger")
          opt[String]('c', "checkpointDir").required().action((x, config) => config.copy(checkpointDir = x)).text("checkpointDir")
          opt[String]('g', "hudiBasePath").required().action((x, config) => config.copy(hudiBasePath = x)).text("hudiBasePath")
          opt[String]('s', "syncDB").required().action((x, config) => config.copy(syncDB = x)).text("syncDB")
          opt[String]('y', "tableType").required().action((x, config) => config.copy(tableType = x)).text("tableType")
          opt[String]('r', "syncJDBCUrl").required().action((x, config) => config.copy(syncJDBCUrl = x)).text("syncJDBCUrl")
          opt[String]('n', "syncJDBCUsername").required().action((x, config) => config.copy(syncJDBCUsername = x)).text("syncJDBCUsername")
      }
    }

    parser.parse(args, AccessLogConf()) match {
      case Some(conf) => conf
      case None =>
        System.exit(-1)
        null
    }
  }
}
