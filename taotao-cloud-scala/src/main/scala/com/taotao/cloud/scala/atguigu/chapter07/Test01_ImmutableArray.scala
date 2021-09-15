package com.taotao.cloud.scala.atguigu.chapter07

object Test01_ImmutableArray {
  def main(args: Array[String]): Unit = {
    // 1. 创建数组
    val arr: Array[Int] = new Array[Int](5)
    // 另一种创建方式
    val arr2 = Array(12, 37, 42, 58, 97)
    println(arr)

    // 2. 访问元素
    println(arr(0))
    println(arr(1))
    println(arr(4))
    //    println(arr(5))

    arr(0) = 12
    arr(4) = 57
    println(arr(0))
    println(arr(1))
    println(arr(4))

    println("========================")

    // 3. 数组的遍历
    // 1) 普通for循环
    for (i <- 0 until arr.length) {
      println(arr(i))
    }

    for (i <- arr.indices) println(arr(i))

    println("---------------------")

    // 2) 直接遍历所有元素，增强for循环
    for (elem <- arr2) println(elem)

    println("---------------------")

    // 3) 迭代器
    val iter = arr2.iterator

    while (iter.hasNext)
      println(iter.next())

    println("---------------------")

    // 4) 调用foreach方法
    arr2.foreach((elem: Int) => println(elem))

    arr.foreach(println)

    println(arr2.mkString("--"))

    println("========================")
    // 4. 添加元素
    val newArr = arr2.:+(73)
    println(arr2.mkString("--"))
    println(newArr.mkString("--"))

    val newArr2 = newArr.+:(30)
    println(newArr2.mkString("--"))

    val newArr3 = newArr2 :+ 15
    val newArr4 = 19 +: 29 +: newArr3 :+ 26 :+ 73
    println(newArr4.mkString(", "))
  }
}
