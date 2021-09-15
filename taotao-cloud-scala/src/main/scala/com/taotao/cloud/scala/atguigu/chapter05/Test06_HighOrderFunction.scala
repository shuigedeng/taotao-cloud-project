package com.taotao.cloud.scala.atguigu.chapter05

object Test06_HighOrderFunction {
  def main(args: Array[String]): Unit = {
    def f(n: Int): Int = {
      println("f调用")
      n + 1
    }

    def fun(): Int = {
      println("fun调用")
      1
    }

    val result: Int = f(123)
    println(result)

    // 1. 函数作为值进行传递
    val f1: Int => Int = f
    val f2 = f _

    println(f1)
    println(f1(12))
    println(f2)
    println(f2(35))

    val f3: () => Int = fun
    val f4 = fun _
    println(f3)
    println(f4)

    // 2. 函数作为参数进行传递
    // 定义二元计算函数
    def dualEval(op: (Int, Int) => Int, a: Int, b: Int): Int = {
      op(a, b)
    }

    def add(a: Int, b: Int): Int = {
      a + b
    }

    println(dualEval(add, 12, 35))
    println(dualEval((a, b) => a + b, 12, 35))
    println(dualEval(_ + _, 12, 35))

    // 3. 函数作为函数的返回值返回
    def f5(): Int => Unit = {
      def f6(a: Int): Unit = {
        println("f6调用 " + a)
      }

      f6 // 将函数直接返回
    }

    //    val f6 = f5()
    //    println(f6)
    //    println(f6(25))

    println(f5()(25))
  }
}
