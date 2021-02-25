package com.taotao.cloud.scala.p3.extendz

/*
	scala中被protected修饰的成员特点
		scala在使用protected和private做访问权限修饰的时候，除了传统的意义以外，还可以做到非常精准的控制某一个成员在某个
		package下面中被访问。
		就在protected|private[具体的报名]
			该成员只能在该包，及其子包下面被访问
		其中有一个特例，就是protected[this],只能在本类及其子类中被使用，不能被子类实例中来进行访问
 */
object ProtectedOps {
	def main(args: Array[String]): Unit = {
		val 小黄 = new Dog("小黄")
		val 大黄 = new Dog("大黄")
		小黄.makeFriends(大黄)
	}
}

class Animal {
	var name:String = _

//	protected[this] var age = 3
//	protected[p3] var age = 3
	private[extendz] var age = 3
	def this(name:String, age:Int) {
		this()
		this.name = name
		this.age = age
	}
	def show(): Unit = {
		println(s"Animal: ${name}, ${age}")
	}
}

class Dog extends Animal {
	age = 5

	def this(name:String) {
		this()
		this.name = name
	}

	def makeFriends(dog: Dog): Unit = {
		println(s"${this.name} age is ${this.age} 要和 ${dog.name}, age is ${dog.age} 交朋友")
	}
}
