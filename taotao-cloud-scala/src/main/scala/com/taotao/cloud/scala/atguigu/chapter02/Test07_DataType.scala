package com.taotao.cloud.scala.atguigu.chapter02

import com.taotao.cloud.scala.atguigu.chapter01.Student


object Test07_DataType {
  def main(args: Array[String]): Unit = {
    // 1. 整数类型
    val a1: Byte = 127
    val a2: Byte = -128

    //    val a2: Byte = 128    // error

    val a3 = 12 // 整数默认类型为Int
    val a4: Long = 1324135436436L // 长整型数值定义

    val b1: Byte = 10
    val b2: Byte = 10 + 20
    println(b2)

    //    val b3: Byte = b1 + 20
    val b3: Byte = (b1 + 20).toByte
    println(b3)

    // 2. 浮点类型
    val f1: Float = 1.2345f
    val d1 = 34.2245

    // 3. 字符类型
    val c1: Char = 'a'
    println(c1)

    val c2: Char = '9'
    println(c2)

    // 控制字符
    val c3: Char = '\t' // 制表符
    val c4: Char = '\n' // 换行符
    println("abc" + c3 + "def")
    println("abc" + c4 + "def")

    // 转义字符
    val c5 = '\\' // 表示\自身
    val c6 = '\"' // 表示"
    println("abc" + c5 + "def")
    println("abc" + c6 + "def")

    // 字符变量底层保存ASCII码
    val i1: Int = c1
    println("i1: " + i1)
    val i2: Int = c2
    println("i2: " + i2)

    val c7: Char = (i1 + 1).toChar
    println(c7)
    val c8: Char = (i2 - 1).toChar
    println(c8)

    // 4. 布尔类型
    val isTrue: Boolean = true
    println(isTrue)

    // 5. 空类型
    // 5.1 空值Unit
    def m1(): Unit = {
      println("m1被调用执行")
    }

    val a: Unit = m1()
    println("a: " + a)

    // 5.2 空引用Null
    //    val n: Int = null    // error
    var student: Student = new Student("alice", 20)
    student = null
    println(student)

    // 5.3 Nothing
    def m2(n: Int): Int = {
      if (n == 0)
        throw new NullPointerException
      else
        n
    }


    val b: Int = m2(2)
    println("b: " + b)
  }
}
