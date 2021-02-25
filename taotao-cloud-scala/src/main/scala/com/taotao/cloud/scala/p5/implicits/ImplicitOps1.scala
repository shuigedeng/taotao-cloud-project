package com.taotao.cloud.scala.p5.implicits

/**
  * 隐式转换：
  *    所谓隐式转换，就是将一种类型，根据需要转化为另外一种类型，并且这个转换的操作不是显示执行，而是隐式执行。
  *    实现这个操作背后的力量——隐式转换函数
  * 隐式转换函数
  *    是一个特别函数，必须有参数，有返回值，其次还要被关键字implicit所修饰。
  *    其常见的定义格式如下：
  *    implicit def source2Target(source:Source): Target = {
  *      //....
  *    }
  *
  */
object ImplicitOps1 {
	def main(args: Array[String]): Unit = {
		val x:Int = 3

		/*
			scala中，就有这么一种将不可能变为可能的能力——隐式转换
		 */
		val y:Int = 3.5f

		val z:Int = "abcdefgdtnlml"

		println("x: " + x)
		println("y: " + y)
		println("z: " + z)
	}

	implicit def float2Int(f: Float): Int = f.intValue()
	implicit def str2Int(str: String): Int = str.length
}
