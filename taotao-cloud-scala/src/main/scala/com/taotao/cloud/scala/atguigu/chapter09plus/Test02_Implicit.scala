package com.taotao.cloud.scala.atguigu.chapter09plus

object Test02_Implicit {
  def main(args: Array[String]): Unit = {

    val new12 = new MyRichInt(12)
    println(new12.myMax(15))

    // 1. 隐式函数
    implicit def convert(num: Int): MyRichInt = new MyRichInt(num)

    println(12.myMax(15))

    println("============================")

    // 2. 隐式类
    implicit class MyRichInt2(val self: Int) {
      // 自定义比较大小的方法
      def myMax2(n: Int): Int = if (n < self) self else n

      def myMin2(n: Int): Int = if (n < self) n else self
    }

    println(12.myMin2(15))

    println("============================")

    // 3. 隐式参数

    implicit val str: String = "alice"
    //    implicit val str2: String = "alice2"
    implicit val num: Int = 18

    def sayHello()(implicit name: String): Unit = {
      println("hello, " + name)
    }

    def sayHi(implicit name: String = "atguigu"): Unit = {
      println("hi, " + name)
    }

    sayHello
    sayHi

    // 简便写法
    def hiAge(): Unit = {
      println("hi, " + implicitly[Int])
    }

    hiAge()
  }
}

// 自定义类
class MyRichInt(val self: Int) {
  // 自定义比较大小的方法
  def myMax(n: Int): Int = if (n < self) self else n

  def myMin(n: Int): Int = if (n < self) n else self
}
