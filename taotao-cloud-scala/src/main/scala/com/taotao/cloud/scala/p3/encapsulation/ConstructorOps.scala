package com.taotao.cloud.scala.p3.encapsulation

/**
  * scala中构造器的定义
  *     1、scala中和类名相同的方法并不是构造器
  *     2、通过分析，scala的其中一个构造器就在类名后面，因为是无参的，所以默认省略的(),也就是参数列表
  *         那问题呢？
  *             1°、该构造器的方法体呢？
  *                 通过代码验证，说明scala类的构造器的函数体和类的定义交至在了一起
  *             2°、可不可以拥有其他构造器？
  *                 scala不能用类名做构造器，而是使用this关键字来代替这里的类名
  *     3、由2的内容，我们就能够归纳如下
  *         1°、scala的构造器有两种，一种在类名后面定义的，一种在类中使用this关键字来定义
  *             第一种的构造器，被称之为scala类的主构造器
  *             第二种的构造器，被称之为scala类的辅助构造器
  *         2°、scala中的辅助构造器的第一句话，必须要调用本类的主构造器或者其他辅助构造器
  */
object ConstructorOps {
	def main(args: Array[String]): Unit = {
		val t = new Teacher()
		t.show()
		t.Teacher()
		println("=============这里使用scala的类有参数构造器=============")
		val t1 = new Teacher("old李", 33)
		t1.show()
		println("=============这里使用scala有参主构造器=============")
		val t2 = new Teacher1("old李", 32)
		t2.show()
	}
}

class Teacher1 /*private 主构造器的私有化*/(name:String, age:Int) {

	def show(): Unit = {
		println(s"name: ${name}, age: ${age}")
	}

}

class Teacher (/*这是scala的默认的类的无参构造器，而且这还是最特殊的一个构造器*/) {
	private var name:String = _
	private var age:Int = _
	println("如果这是类的构造器的话，该语句将会被调用~")
	def Teacher(): Unit = {
		println("如果这是类的构造器的话，该方法将会被调用~")
	}

	def this(name:String, age:Int) {
		this()
		this.name = name
		this.age = age
		println("如果这是类的有参构造器的话，该语句将会被调用~")
	}

	def show(): Unit = {
		println(s"name: ${name}, age: ${age}")
	}
}
