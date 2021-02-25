package com.taotao.cloud.scala.p3.abstracts

/**
  * scala中的抽象字段：
  *     所有抽象字段，就是只有字段的声明，没有初始化的字段
  */
object AbstractOps2 {
	def main(args: Array[String]): Unit = {
		val az:AbstractFu = new AbstractZi
		println("name: " + az.name)
		println("age: " + az.age)
	}
}

abstract class AbstractFu {
	//定义了一个抽象的val和var的字段
	val name:String
	var age:Int
}

class AbstractZi extends AbstractFu {
	override val name = "zhangsan"
	/*override*/ var age = 13
}
