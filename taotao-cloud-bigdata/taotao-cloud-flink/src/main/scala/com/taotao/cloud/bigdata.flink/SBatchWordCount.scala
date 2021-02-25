package com.taotao.cloud.bigdata.flink

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

/**
 * SBatchWordCount
 *
 * @author dengtao
 * @since 2020/11/3 09:05
 * @version 1.0.0
 */
object SBatchWordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val lines = env.readTextFile("/Users/dengtao/spark/hello.txt")

    lines.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(_._1)
      .sum(1)
      .print()

    Thread.sleep(Long.MaxValue)

    env.execute("SBatchWordCount")

  }
}
