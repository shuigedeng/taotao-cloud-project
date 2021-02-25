package com.taotao.cloud.flink

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

/**
 * JBatchWordCount
 *
 * nc -lk 8888
 *
 * @author dengtao
 * @since 2020/11/3 09:05
 * @version 1.0.0
 */
object SStreamWordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val lines = env.socketTextStream("localhost", 8888)

    lines.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(0)
      .sum(1)
      .print()

    env.execute("SStreamWordCount")
  }
}
