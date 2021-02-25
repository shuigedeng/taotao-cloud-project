package com.taotao.cloud.scala.p3.abstracts

/*
	scala中的抽象类
		1.scala中一个类中的方法或者字段只做了一个声明，没有进行实现或者初始化，
		我们把这这类就称之为抽象类，需要使用关键字abstract来进行声明
		2.scala中的抽象类和java一样，既可以有抽象成员，也可以有非抽象成员
		3.scala类中字类复写父类的抽象成员的时候，我们可以省略掉override关键字
		4.scala中的抽象成员，可以省略abstract关键字
 */
object AbstractOps1 {
	def main(args: Array[String]): Unit = {
		val bat:Animal = new Bat
		bat.sleep()
		bat.dead()

		val dog:Animal = new Dog
		dog.sleep()
		dog.dead()
	}
}

abstract class Animal {
	var color:String = _
	//只有方法的定义，没有方法的实现，这就是一个抽象方法
	/*abstract */def sleep()

	def dead(): Unit = {
		println("动物借有一死，或清蒸，或红烧，或爆炒")
	}
}

class Bat extends Animal {
	color = "黑色"
	/*override */def sleep(): Unit = {
		println("倒挂金钟，以打呼噜~~~")
	}

	override def dead(): Unit = {
		println("蝙蝠吃的爽，icu躺倒老~")
	}
}

class Dog extends Animal {
	color = "小黄"
	override def sleep(): Unit = {
		println("小狗一般都趴着睡觉")
	}

	override def dead(): Unit = {
		println("小狗是人类忠实好伙伴，千万要爱惜")
	}
}
