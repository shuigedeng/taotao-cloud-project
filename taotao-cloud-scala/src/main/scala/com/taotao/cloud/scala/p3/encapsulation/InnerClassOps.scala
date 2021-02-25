package com.taotao.cloud.scala.p3.encapsulation

/**
  * scala版本的内部类
  */
object InnerClassOps {
	def main(args: Array[String]): Unit = {
		val outer:Outer = new Outer()

//		val inner:outer.Inner = new outer.Inner()
		//创建scala的内部类对象
		val inner = new outer.Inner()
		inner.show()

	}
}

class Outer { o => //此时的o代表的就是本类对象的引用
	val x = 5
	class Inner { i => //此时的i代表的就是本类对象的引用
		val x = 6

		def show(): Unit = {
			val x = 7
			println("x = " + x)
			println("x = " + this.x)
			println("i.x = " + i.x)
			println("x = " + Outer.this.x)
			println("o.x = " + o.x)
		}
	}
}
