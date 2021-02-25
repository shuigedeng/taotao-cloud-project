package com.taotao.cloud.scala.p3.extendz

/**
  * scala中的继承/扩展
  *     1. 使用关键字extends来产生这种继承或者扩展的关系
  *     2. 子类可以继承父类中所有非私有的成员
  *     3. 子类不能覆盖父类中被final所修饰的成员
  *     4. scala中子类覆盖父类的方法时，必须要添加关键字override进行修饰，以标识要对父类方法进行覆盖，
  *         否则认为子类重新一个同名方法，这就会造成方法同名，会报错，除非该方法是抽象方法。
  *     5. 子类要想访问父类的成员的时候，就需要使用super关键字来完成
  */
object ExtendsOps1 {
	def main(args: Array[String]): Unit = {
		val stu: Person = new Student("王二麻子")
		stu.show()
	}
}
class Person {
	private var name:String = _
	var age:Int = 13

	def this(name:String, age: Int) {
		this()
		this.name = name
		this.age = age
	}

	def show(): Unit = {
		println(s"person-name: ${name}, age: ${age}")
	}

	def setName(name:String) = this.name = name
}

class Student extends Person {

	def this(name:String) {
		this()
		//完成name的赋值
//		this.name = name
		super.setName(name)
	}
	override def show(): Unit = {
		//调用父类的show方法
		super.show()
		println("---student---show")
	}
}
