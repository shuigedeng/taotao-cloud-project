package com.taotao.cloud.scala.p3.extendz

/**
  *  scala中的匿名子类---匿名内部类
  *     匿名的意思，就是说该类名名称
  *  通常出现在继承体系中，对于接口，抽象类只被调用一次的情况，同时父类中的（抽象）方法相对较少。
  *
  *  class Xxx{} --->这可不是匿名的
  *
  *  new Xxx(){} --->匿名子类
  *
  *  常见的作用：作为方法的参数出现
  */
object AnnominyInnerClassOps {
	def main(args: Array[String]): Unit = {
		showName(new NameAnnomiy)//调用非匿名子类来实现
		println("-----------------通过匿名子类来实现-----------------------")
		val annominy = new Annominy(){
			//这是Annominy的一个匿名子类
			override def show(): Unit = {
				super.show()
				println("待到春暖花开时，迎接武汉抗疫英雄儿女的凯旋~")
			}
			def myPrint(): Unit = {
				println("只要人人都献出一点爱，世界将会变得还精彩~")
			}
		}
		annominy.show()
		annominy.myPrint()
		showName(annominy)
		println("-----------------通过局部匿名内部来实现-----------------------")
		showName(new Annominy(){
			override def show(): Unit = {
				super.show()
				println("待到春暖花开时，迎接武汉抗疫英雄儿女的凯旋~")
			}
		})

	}

	def showName(annominy:Annominy): Unit = {
		annominy.show()
	}
}
class Annominy {
	val name:String = "我们都是武汉人，我们为武汉加油！！！"

	def show(): Unit = {
		println(name)
	}
}
//非匿名子类
class NameAnnomiy extends Annominy {

}
