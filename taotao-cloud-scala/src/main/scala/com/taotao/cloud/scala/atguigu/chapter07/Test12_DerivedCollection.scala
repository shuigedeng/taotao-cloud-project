package com.taotao.cloud.scala.atguigu.chapter07

object Test12_DerivedCollection {
  def main(args: Array[String]): Unit = {
    val list1 = List(1, 3, 5, 7, 2, 89)
    val list2 = List(3, 7, 2, 45, 4, 8, 19)

    //    （1）获取集合的头
    println(list1.head)

    //    （2）获取集合的尾（不是头的就是尾）
    println(list1.tail)

    //    （3）集合最后一个数据
    println(list2.last)

    //    （4）集合初始数据（不包含最后一个）
    println(list2.init)

    //    （5）反转
    println(list1.reverse)

    //    （6）取前（后）n个元素
    println(list1.take(3))
    println(list1.takeRight(4))

    //    （7）去掉前（后）n个元素
    println(list1.drop(3))
    println(list1.dropRight(4))

    println("=========================")
    //    （8）并集
    val union = list1.union(list2)
    println("union: " + union)
    println(list1 ::: list2)

    // 如果是set做并集，会去重
    val set1 = Set(1, 3, 5, 7, 2, 89)
    val set2 = Set(3, 7, 2, 45, 4, 8, 19)

    val union2 = set1.union(set2)
    println("union2: " + union2)
    println(set1 ++ set2)

    println("-----------------------")
    //    （9）交集
    val intersection = list1.intersect(list2)
    println("intersection: " + intersection)

    println("-----------------------")
    //    （10）差集
    val diff1 = list1.diff(list2)
    val diff2 = list2.diff(list1)
    println("diff1: " + diff1)
    println("diff2: " + diff2)

    println("-----------------------")
    //    （11）拉链
    println("zip: " + list1.zip(list2))
    println("zip: " + list2.zip(list1))

    println("-----------------------")
    //    （12）滑窗
    for (elem <- list1.sliding(3))
      println(elem)
    println("-----------------------")

    for (elem <- list2.sliding(4, 2))
      println(elem)

    println("-----------------------")
    for (elem <- list2.sliding(3, 3))
      println(elem)
  }
}
