package com.taotao.cloud.scala.atguigu.chapter05

object Test01_FunctionAndMethod {
  def main(args: Array[String]): Unit = {
    // 定义函数
    def sayHi(name: String): Unit = {
      println("hi, " + name)
    }

    // 调用函数
    sayHi("alice")

    // 调用对象方法
    Test01_FunctionAndMethod.sayHi("bob")

    // 获取方法返回值
    val result = Test01_FunctionAndMethod.sayHello("cary")
    println(result)
  }

  // 定义对象的方法
  def sayHi(name: String): Unit = {
    println("Hi, " + name)
  }

  def sayHello(name: String): String = {
    println("Hello, " + name)
    return "Hello"
  }
}
