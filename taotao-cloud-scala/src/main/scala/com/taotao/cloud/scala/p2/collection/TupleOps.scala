package com.taotao.cloud.scala.p2.collection

/*
	scala集合体系之tuple元组的操作
 */
object TupleOps {
	def main(args: Array[String]): Unit = {
		//构建一个元组（不可变的集合）
		val tuple1 = new Tuple2[String, Int]("张三", 13)
		val tuple2 = ("张三", 13)

		//获取元组中的值
		val name = tuple1._1
		val age = tuple1._2

		println(s"name: ${name}, age: ${age}")

		//推荐元组的定义方式
		val t3 = ("spring", "summer", "autumn", "winter")
		val (spring, summer, autumn, winter) = ("spring", "summer", "autumn", "winter")

		println(spring == t3._1)
		println(summer == t3._2)
		println(autumn == t3._3)
		println(winter == t3._4)

		//元组的遍历
		for(t <- t3.productIterator) {
			println(t)
		}

	}
}
