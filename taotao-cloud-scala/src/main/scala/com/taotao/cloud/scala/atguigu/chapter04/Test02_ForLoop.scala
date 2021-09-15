package com.taotao.cloud.scala.atguigu.chapter04

import scala.collection.immutable
import scala.language.postfixOps
import scala.math.BigDecimal.double2bigDecimal

object Test02_ForLoop {
  def main(args: Array[String]): Unit = {
    // java for语法： for(int i = 0; i < 10; i++){ System.out.println(i + ". hello world") }

    // 1. 范围遍历
    for (i <- 1 to 10) {
      println(i + ". hello world")
    }
    for (i: Int <- 1.to(10)) {
      println(i + ". hello world")
    }

    println("===========================")
    //    for (i <- Range(1, 10)){
    //      println(i + ". hello world")
    //    }
    for (i <- 1 until 10) {
      println(i + ". hello world")
    }

    println("==============================")
    // 2. 集合遍历
    for (i <- Array(12, 34, 53)) {
      println(i)
    }
    for (i <- List(12, 34, 53)) {
      println(i)
    }
    for (i <- Set(12, 34, 53)) {
      println(i)
    }

    println("==========================")

    // 3. 循环守卫
    for (i <- 1 to 10) {
      if (i != 5) {
        println(i)
      }
    }

    for (i <- 1 to 10 if i != 5) {
      println(i)
    }

    println("======================")

    // 4. 循环步长
    for (i <- 1 to 10 by 2) {
      println(i)
    }
    println("-------------------")
    for (i <- 13 to 30 by 3) {
      println(i)
    }

    println("-------------------")
    for (i <- 30 to 13 by -2) {
      println(i)
    }
    for (i <- 10 to 1 by -1) {
      println(i)
    }
    println("-------------------")
    for (i <- 1 to 10 reverse) {
      println(i)
    }
    println("-------------------")
    //    for (i <- 30 to 13 by 0){
    //      println(i)
    //    }    // error，step不能为0

    for (data <- 1.0 to 10.0 by 0.3) {
      println(data)
    }

    println("======================")

    // 5. 循环嵌套
    for (i <- 1 to 3) {
      for (j <- 1 to 3) {
        println("i = " + i + ", j = " + j)
      }
    }
    println("-------------------")
    for (i <- 1 to 4; j <- 1 to 5) {
      println("i = " + i + ", j = " + j)
    }

    println("======================")

    // 6. 循环引入变量
    for (i <- 1 to 10) {
      val j = 10 - i
      println("i = " + i + ", j = " + j)
    }

    for (i <- 1 to 10; j = 10 - i) {
      println("i = " + i + ", j = " + j)
    }

    for {
      i <- 1 to 10
      j = 10 - i
    } {
      println("i = " + i + ", j = " + j)
    }

    println("======================")

    // 7. 循环返回值
    val a = for (i <- 1 to 10) {
      i
    }
    println("a = " + a)

    val b: immutable.IndexedSeq[Int] = for (i <- 1 to 10) yield i * i
    println("b = " + b)
  }
}
