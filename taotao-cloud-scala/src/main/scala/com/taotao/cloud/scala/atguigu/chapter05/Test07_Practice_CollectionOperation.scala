package com.taotao.cloud.scala.atguigu.chapter05

object Test07_Practice_CollectionOperation {
  def main(args: Array[String]): Unit = {
    val arr: Array[Int] = Array(12, 45, 75, 98)

    // 对数组进行处理，将操作抽象出来，处理完毕之后的结果返回一个新的数组
    def arrayOperation(array: Array[Int], op: Int => Int): Array[Int] = {
      for (elem <- array) yield op(elem)
    }

    // 定义一个加一操作
    def addOne(elem: Int): Int = {
      elem + 1
    }

    // 调用函数
    val newArray: Array[Int] = arrayOperation(arr, addOne)

    println(newArray.mkString(","))

    // 传入匿名函数，实现元素翻倍
    val newArray2 = arrayOperation(arr, _ * 2)
    println(newArray2.mkString(", "))
  }
}
