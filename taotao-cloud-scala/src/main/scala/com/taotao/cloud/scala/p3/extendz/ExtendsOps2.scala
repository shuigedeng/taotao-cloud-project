package com.taotao.cloud.scala.p3.extendz

/*
	scala中超类的构造过程

		1.scala中在继承体系中，构造子类的时候，现有构造父类
		2.子类在构造器中要想传递参数到父类的构造器中，只能通过主构造器
			因为辅助构造器的第一句话，只能调用本类的主构造器，或者其他辅助构造器
 */
object ExtendsOps2 {
	def main(args: Array[String]): Unit = {
		val zi:Fu = new Zi(13)
	}
}

class Fu (name:String, age:Int) {

	println("-------------Fu-primary-constructor--------------")

	def this(name:String) {
		this(name, 13)
		println("--------Fu--this(name:String)--constructor------")
	}
}

class Zi(name:String, age:Int) extends Fu(name) {

	println("-------------Zi-primary-constructor--------------")

	def this(age:Int) {
		this("zhangsan", age)
		println("--------Zi--this(age:Int)--constructor------")
	}
}
