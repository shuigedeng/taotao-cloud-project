package com.taotao.cloud.scala.atguigu.chapter05

object Test09_ClosureAndCurrying {
  def main(args: Array[String]): Unit = {
    def add(a: Int, b: Int): Int = {
      a + b
    }

    // 1. 考虑固定一个加数的场景
    def addByFour(b: Int): Int = {
      4 + b
    }

    // 2. 扩展固定加数改变的情况
    def addByFive(b: Int): Int = {
      5 + b
    }

    // 3. 将固定加数作为另一个参数传入，但是是作为”第一层参数“传入
    def addByFour1(): Int => Int = {
      val a = 4

      def addB(b: Int): Int = {
        a + b
      }

      addB
    }

    def addByA(a: Int): Int => Int = {
      def addB(b: Int): Int = {
        a + b
      }

      addB
    }

    println(addByA(35)(24))

    val addByFour2: Int => Int = addByA(4)
    val addByFive2: Int => Int = addByA(5)

    println(addByFour2(13))
    println(addByFive2(25))

    // 4. lambda表达式简写
    def addByA1(a: Int): Int => Int = {
      (b: Int) => {
        a + b
      }
    }

    def addByA2(a: Int): Int => Int = {
      b => a + b
    }

    def addByA3(a: Int): Int => Int = a + _

    val addByFour3: Int => Int = addByA3(4)
    val addByFive3: Int => Int = addByA3(5)

    println(addByFour3(13))
    println(addByFive3(25))

    // 5. 柯里化
    def addCurrying(a: Int)(b: Int): Int = {
      a + b
    }

    println(addCurrying(35)(24))
  }
}
