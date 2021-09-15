package com.taotao.cloud.scala.atguigu.chapter07

import scala.collection.mutable

object Test07_MutableSet {
  def main(args: Array[String]): Unit = {
    // 1. 创建set
    val set1: mutable.Set[Int] = mutable.Set(13, 23, 53, 12, 13, 23, 78)
    println(set1)

    println("==================")

    // 2. 添加元素
    val set2 = set1 + 11
    println(set1)
    println(set2)

    set1 += 11
    println(set1)

    val flag1 = set1.add(10)
    println(flag1)
    println(set1)
    val flag2 = set1.add(10)
    println(flag2)
    println(set1)

    println("==================")

    // 3. 删除元素
    set1 -= 11
    println(set1)

    val flag3 = set1.remove(10)
    println(flag3)
    println(set1)
    val flag4 = set1.remove(10)
    println(flag4)
    println(set1)

    println("==================")

    // 4. 合并两个Set
  }
}
