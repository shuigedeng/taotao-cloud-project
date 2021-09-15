package com.taotao.cloud.scala.atguigu.chapter05

object Test08_Practice {
  def main(args: Array[String]): Unit = {
    // 1. 练习1
    val fun: (Int, String, Char) => Boolean = (i: Int, s: String, c: Char) => {
      if (i == 0 && s == "" && c == '0') false else true
    }

    println(fun(0, "", '0'))
    println(fun(0, "", '1'))
    println(fun(23, "", '0'))
    println(fun(0, "hello", '0'))

    println("===========================")

    // 2. 练习2
    def func(i: Int): String => (Char => Boolean) = {
      def f1(s: String): Char => Boolean = {
        def f2(c: Char): Boolean = {
          if (i == 0 && s == "" && c == '0') false else true
        }
        f2
      }
      f1
    }

    println(func(0)("")('0'))
    println(func(0)("")('1'))
    println(func(23)("")('0'))
    println(func(0)("hello")('0'))

    // 匿名函数简写
    def func1(i: Int): String => (Char => Boolean) = {
      s => c => if (i == 0 && s == "" && c == '0') false else true
    }

    println(func1(0)("")('0'))
    println(func1(0)("")('1'))
    println(func1(23)("")('0'))
    println(func1(0)("hello")('0'))

    // 柯里化
    def func2(i: Int)(s: String)(c: Char): Boolean = {
      if (i == 0 && s == "" && c == '0') false else true
    }

    println(func2(0)("")('0'))
    println(func2(0)("")('1'))
    println(func2(23)("")('0'))
    println(func2(0)("hello")('0'))
  }
}
