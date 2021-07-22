package com.taotao.cloud.scala.atguigu.chapter06

object Test05_Constructor {
  def main(args: Array[String]): Unit = {
    val student1 = new Student1
    student1.Student1()

    val student2 = new Student1("alice")
    val student3 = new Student1("bob", 25)
  }
}

// 定义一个类
class Student1() {
  // 定义属性
  var name: String = _
  var age: Int = _

  println("1. 主构造方法被调用")

  // 声明辅助构造方法
  def this(name: String) {
    this()    // 直接调用主构造器
    println("2. 辅助构造方法一被调用")
    this.name = name
    println(s"name: $name age: $age")
  }

  def this(name: String, age: Int){
    this(name)
    println("3. 辅助构造方法二被调用")
    this.age = age
    println(s"name: $name age: $age")
  }

  def Student1(): Unit = {
    println("一般方法被调用")
  }
}
