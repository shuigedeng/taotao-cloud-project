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
package com.taotao.cloud.hudi.extract

import com.alibaba.fastjson2.{JSON, JSONObject}
import org.slf4j.{Logger, LoggerFactory}

import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.Base64

class LogExtract extends java.io.Serializable {
  val LOGGER: Logger = LoggerFactory.getLogger("LogExtract")

  def unionMetaAndBody(base64Line: String): String = {
    try {
      val textArray: Array[String] = base64Line.split("-")
      if (textArray.length != 2) {
        return null
      }

      val metaBytes: Array[Byte] = Base64.getDecoder.decode(textArray(0))
      val meta: String = new String(metaBytes)

      val bodyBytes: Array[Byte] = Base64.getDecoder.decode(textArray(1))
      val body: String = new String(bodyBytes)

      val metaJson: JSONObject = JSON.parseObject(meta);
      val bodyJson: JSONObject = JSON.parseObject(body);

      val unionJson: JSONObject = new JSONObject()
      val propertiesJson: JSONObject = bodyJson.getJSONObject("properties")
      val libJson: JSONObject = bodyJson.getJSONObject("lib")
      unionJson.putAll(propertiesJson.asInstanceOf[java.util.Map[String, _]])
      unionJson.putAll(libJson.asInstanceOf[java.util.Map[String, _]])

      bodyJson.remove("properties")
      bodyJson.remove("lib")

      unionJson.putAll(bodyJson.asInstanceOf[java.util.Map[String, _]])
      unionJson.putAll(metaJson.asInstanceOf[java.util.Map[String, _]])

      val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
      val serverTime: BigInteger = metaJson.getBigInteger("ctime")
      val logDay: String = sdf.format(serverTime)

      unionJson.put("logday", logDay)
      unionJson.put("uuid", logDay)

      //      val json: JSONObject = JSON.parseObject(jsonStr)
      //      unionJson.forEach((key, value) => {
      //        json.put(key.trim(), value)
      //      })

      val result: JSONObject = new JSONObject()
      unionJson.forEach((key, value) => {
        if (key.startsWith("$") || key.startsWith("_") || key.startsWith("%")) {
          result.put(key.substring(1, key.length).trim(), value)
        } else {
          result.put(key.trim(), value)
        }
      })

      LOGGER.info("result==============" + result)
      result.toJSONString
    } catch {
      case e: Exception =>
        try {
          val dataBytes: Array[Byte] = Base64.getDecoder.decode(base64Line)
          val data: String = new String(dataBytes)
          val dataJson: JSONObject = JSON.parseObject(data);

          val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
          val serverTime: String = dataJson.getString("ctime")
          val logDay: String = sdf.format(serverTime)

          dataJson.put("logday", logDay)
          dataJson.put("uuid", logDay)

          dataJson.toJSONString
        } catch {
          case error: Exception =>
            null
        }
    }
  }
}
