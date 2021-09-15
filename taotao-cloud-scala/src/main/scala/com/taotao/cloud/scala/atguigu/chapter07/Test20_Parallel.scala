package com.taotao.cloud.scala.atguigu.chapter07

import scala.collection.immutable

object Test20_Parallel {
  def main(args: Array[String]): Unit = {
    val result: immutable.IndexedSeq[Long] = (1 to 100).map(
      x => Thread.currentThread.getId
    )
    println(result)

    val result2 = ((1 to 100)).map(
      x => Thread.currentThread.getId
    )
    println(result2)
  }
}
