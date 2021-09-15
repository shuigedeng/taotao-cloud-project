package com.taotao.cloud.scala.atguigu.chapter07

object Test04_List {
  def main(args: Array[String]): Unit = {
    // 1. 创建一个List
    val list1 = List(23, 65, 87)
    println(list1)

    // 2. 访问和遍历元素
    println(list1(1))
    //    list1(1) = 12
    list1.foreach(println)

    // 3. 添加元素
    val list2 = 10 +: list1
    val list3 = list1 :+ 23
    println(list1)
    println(list2)
    println(list3)

    println("==================")

    val list4 = list2.::(51)
    println(list4)

    val list5 = Nil.::(13)
    println(list5)

    val list6 = 73 :: 32 :: Nil
    val list7 = 17 :: 28 :: 59 :: 16 :: Nil
    println(list7)

    // 4. 合并列表
    val list8 = list6 :: list7
    println(list8)

    val list9 = list6 ::: list7
    println(list9)

    val list10 = list6 ++ list7
    println(list10)

  }
}
