package com.taotao.cloud.scala.atguigu.chapter06

object Test08_DynamicBind {
  def main(args: Array[String]): Unit = {
    val student: Person8 = new Student8
    println(student.name)
    student.hello()
  }
}

class Person8 {
  val name: String = "person"

  def hello(): Unit = {
    println("hello person")
  }
}

class Student8 extends Person8 {
  override val name: String = "student"

  override def hello(): Unit = {
    println("hello student")
  }
}
