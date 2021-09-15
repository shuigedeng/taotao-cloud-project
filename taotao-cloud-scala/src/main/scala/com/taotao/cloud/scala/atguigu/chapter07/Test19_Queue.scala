package com.taotao.cloud.scala.atguigu.chapter07

import scala.collection.immutable.Queue
import scala.collection.mutable

object Test19_Queue {
  def main(args: Array[String]): Unit = {
    // 创建一个可变队列
    val queue: mutable.Queue[String] = new mutable.Queue[String]()

    queue.enqueue("a", "b", "c")

    println(queue)
    println(queue.dequeue())
    println(queue)
    println(queue.dequeue())
    println(queue)

    queue.enqueue("d", "e")

    println(queue)
    println(queue.dequeue())
    println(queue)

    println("==========================")

    // 不可变队列
    val queue2: Queue[String] = Queue("a", "b", "c")
    val queue3 = queue2.enqueue("d")
    println(queue2)
    println(queue3)

  }
}
