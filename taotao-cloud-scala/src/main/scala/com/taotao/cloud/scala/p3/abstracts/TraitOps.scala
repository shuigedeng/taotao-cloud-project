package com.taotao.cloud.scala.p3.abstracts

/*
scala中的特质trait
	什么是trait特质？
		特质trait，就是在scala中一种和class、object同一级别的语法结构，需要被trait关键字进行声明。
	为什么要有trait特质？
		java和scala的继承特点？
			不仅仅指的是：封装、继承、多态、抽象。
			只能进行单继承，不可以进行多继承！
		这种单继承，是有局限的，只能单一的继承某一个类的成员，在scala和java中是无法做到再去继承其他类的特性。
		为了弥补这个缺陷，在scala和java中，首先就有多层继承。
			A extend B, B extends C <=> A继承了B，同时也继承C的特性
		其次在java中还有一个多实现的概念，被实现的结构称之为井口(interface)，接口中的所有的方法都是抽象的。

		===》
		要进行实现的所有的方法都是抽象，那如果一个接口的方法很多，在实现起来很麻烦，所以再java中又有一种设计模式——适配器。

		于是乎，在scala中就弥补了这个设计上的缺憾——java中多实现的方法都需要自己来实现。那么也就是说，在scala中是可以多实现的，同时
		多实现的方法不一定都是抽象。

		scala中把类似java中的这种多实现接口(interface)，不叫多实现，而称之为trait特质的多扩展。
		scala中的trait在一定程度上，是可以看做java中的接口interface
			如果一个trait特质中的所有的方法都是抽象的，那么就可以将其视作为java中的一个接口。

		在java中，实现多个接口的时候，使用的是关键字implements，多个使用","来进行分割
		在scala中，扩展特质trait和扩展父类使用的是相同的关键字extends，扩展多个trait的时候使用with关键字进行分割
		而且，如果即继承一个类，又扩展一个特质，书写的顺序是继承类有限，其次在with特质


 */
object TraitOps {
	def main(args: Array[String]): Unit = {
		val consoleLog = new ConsoleLog with TraitMix
		consoleLog.log("行车不规范，亲人两行泪")
		consoleLog.show()
		consoleLog.method()

		//混入
		consoleLog.mix
	}
}

class TraitFu {
	def method(): Unit = {
		println("---TraitFu-method---")
	}
}
/*
	在运行时，让一个类具备另外一个类的特征，把这种操作，称之为scala中的混入，此时的影响只是在局部
	只需要在创建类对象的时候，使用关键字with连接需要具备的类即可
 */
trait TraitMix {
	def mix(): Unit = {
		println("这是为了证明scala中的特质还有更NB的地方——混入~")
	}
}

trait Log {
	def log(msg:String) //一个抽象方法
	def show(): Unit = {
		println("这就是为了证明，scala中的trait要比java中的interface接口更加的NB~")
	}
}

class ConsoleLog extends TraitFu with Log with Serializable {
	override def log(msg: String): Unit = {
		println("控制台： " + msg)
	}
}

class FileLog extends Log {
	override def log(msg: String): Unit = {
		println("文件： " + msg)
	}
}

