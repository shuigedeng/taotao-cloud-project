package com.taotao.cloud.scala.atguigu

package object chapter06 {
  // 定义当前包共享的属性和方法
  val commonValue = "尚硅谷"

  def commonMethod() = {
    println(s"我们在${commonValue}学习")
  }
}
