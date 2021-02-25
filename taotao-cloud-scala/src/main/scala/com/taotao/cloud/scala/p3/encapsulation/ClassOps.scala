package com.taotao.cloud.scala.p3.encapsulation

import scala.beans.BeanProperty

/*
	scala的类的定义

		scala和java一样，都是用关键字class来进行类的声明

	这里创建的对象Person，其实就是一个javabean，这种对象在java的主要作用是什么？
		其实就是在各个调度层之间携带并传递数据
		controller ----> service -----> dao
				   bean            bean
		controller <---- service <----- dao
	创建的这些bean一般都只会存在字段，而很少创建一些相关的方法

	在scala中刚刚创建的这个类，其实就是模拟了java中的Javabean，当然在scala中有一个专门的类结构
	来表示java中的bean——case class（样例类），会自动的给其中的字段提供的getter和setter方法
 */
object ClassOps {
	def main(args: Array[String]): Unit = {
		//创建类的对象
		val p:Person = new Person()
		p.setName("张三")
		p.setAge(13)
//		println(s"name: ${p.getName}, age: ${p.getAge()}")
		p.show()
		println("------------------------------")
		val stu = new Student
		stu.setName("李四")
		stu.setAge(14)
		stu.age = -1//这是有大大的问题的
		stu.show()
	}
}

//定义了一个scala的类，类名叫Person
class Person {
	private var name:String = _ //定义了一个String类型的成员--name，并赋予默认值，默认值为null

	private var age:Int = _ //定义了一个Int类型的成员--age，并赋予默认值，默认值为0

	//提供getter和setter来处理
	def setName(n:String): Unit = {
		name = n
	}
	def getName = name //前面学习过的单行函数

	def setAge(a:Int): Unit = {
		if(a < 1 || a >= 150) {
			throw new RuntimeException("年龄不能小于1, 或者不能超过150岁")
		}
		age = a
	}

	def getAge() = age

	def show(): Unit = {
		println(s"name: ${name}, age: ${age}")
	}
}

//要想给student的成员变量自动的添加getter和setter只需要添加一个注解即可
class Student {

	@BeanProperty var name:String = _

	@BeanProperty var age:Int = _

	def show(): Unit = {
		println(s"name: ${name}, age: ${age}")
	}
}
