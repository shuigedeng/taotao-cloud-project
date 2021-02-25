package com.taotao.cloud.scala.p3.encapsulation

/*
scala中的伴生对象
	scala中把同一个源文件中相同名称的object结构诚挚为class结构的伴生对象，反过来，把这个class结构的类称之为
object结构的伴生类

class Singleton 是object Singleton的伴生类
object Singleton是class Singleton 的伴生对象

	构建伴生类/伴生对象成立的几个前提
		1、二者必须在同一个.class源文件
		2、二者名称必须相同
	伴生类/伴生对象的特点
		1、伴生对象可以访问伴生类中非私有和私有的成员
		2、通过我们需要在伴生对象中区覆盖一个方法--apply，用于构造伴生类的实例
			比如，Array，ArrayBuffer，Map等等都是通过半生对象来创建对象，该构造的时候其实就是调用了该伴生对象apply方法
		该apply方法的特点：
			1°、返回值类型是本类引用
			2°、参数列表对应伴生类的构造器的参数列表
		3、有了伴生对象，同时覆盖了apply方法，便给半生类提供了一个简化的对象构造器方式，即可以省略掉new关键

 */
object CompanionObjecctOps {
	def main(args: Array[String]): Unit = {
		val cc = new CompanionClass()
		println("---------------------------")
		val cc1 = CompanionClass
		println("---------------------------")
		val cc2 = CompanionClass("zhangsan", 13)


	}
}

class CompanionClass {
	private val x = 6;

	def this(name:String, age:Int) {
		this()
		println("name: " + name)
		println("age: " + age)
	}
}

object CompanionClass {


	def apply(): CompanionClass = {
		new CompanionClass()
	}

	def apply(name:String, age:Int): CompanionClass = {
		val cc = new CompanionClass(name, age)
		println(cc.x)
		cc
	}
}
