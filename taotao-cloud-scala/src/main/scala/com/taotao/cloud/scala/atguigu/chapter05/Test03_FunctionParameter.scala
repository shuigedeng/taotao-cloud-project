package com.taotao.cloud.scala.atguigu.chapter05

object Test03_FunctionParameter {
  def main(args: Array[String]): Unit = {
    //    （1）可变参数
    def f1(str: String*): Unit = {
      println(str)
    }

    f1("alice")
    f1("aaa", "bbb", "ccc")

    //    （2）如果参数列表中存在多个参数，那么可变参数一般放置在最后
    def f2(str1: String, str2: String*): Unit = {
      println("str1: " + str1 + " str2: " + str2)
    }

    f2("alice")
    f2("aaa", "bbb", "ccc")

    //    （3）参数默认值，一般将有默认值的参数放置在参数列表的后面
    def f3(name: String = "atguigu"): Unit = {
      println("My school is " + name)
    }

    f3("school")
    f3()

    //    （4）带名参数
    def f4(name: String = "atguigu", age: Int): Unit = {
      println(s"${age}岁的${name}在尚硅谷学习")
    }

    f4("alice", 20)
    f4(age = 23, name = "bob")
    f4(age = 21)
  }
}
