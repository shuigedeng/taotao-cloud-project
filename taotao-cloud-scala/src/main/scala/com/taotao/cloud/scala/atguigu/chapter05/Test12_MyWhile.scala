package com.taotao.cloud.scala.atguigu.chapter05

object Test12_MyWhile {
  def main(args: Array[String]): Unit = {
    var n = 10

    // 1. 常规的while循环
    while (n >= 1) {
      println(n)
      n -= 1
    }

    // 2. 用闭包实现一个函数，将代码块作为参数传入，递归调用
    def myWhile(condition: => Boolean): (=> Unit) => Unit = {
      // 内层函数需要递归调用，参数就是循环体
      def doLoop(op: => Unit): Unit = {
        if (condition) {
          op
          myWhile(condition)(op)
        }
      }

      doLoop _
    }

    println("=================")
    n = 10
    myWhile(n >= 1) {
      println(n)
      n -= 1
    }

    // 3. 用匿名函数实现
    def myWhile2(condition: => Boolean): (=> Unit) => Unit = {
      // 内层函数需要递归调用，参数就是循环体
      op => {
        if (condition) {
          op
          myWhile2(condition)(op)
        }
      }
    }

    println("=================")
    n = 10
    myWhile2(n >= 1) {
      println(n)
      n -= 1
    }

    // 3. 用柯里化实现
    def myWhile3(condition: => Boolean)(op: => Unit): Unit = {
      if (condition) {
        op
        myWhile3(condition)(op)
      }
    }

    println("=================")
    n = 10
    myWhile3(n >= 1) {
      println(n)
      n -= 1
    }
  }
}
