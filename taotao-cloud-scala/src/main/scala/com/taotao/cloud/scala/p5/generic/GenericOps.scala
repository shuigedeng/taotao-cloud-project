package com.taotao.cloud.scala.p5.generic

/*
	scala泛型的协变和逆变：
		在默认情况，scala中“=”左右两侧的泛型关系和java中是一致，不能允许出现继承关系，
		但是scala我们可以来修订这个相等的关系
 */
class GenericOps {
	def main(args: Array[String]): Unit = {
		val myList: MyList[Person] = new MyList[Person]
		val myList1: MyList1[Person] = new MyList1[Student]
		val myList2: MyList2[Student] = new MyList2[Person]
	}
}

class Person {}
class Student extends Person {}
class Worker extends Person {}

class MyList[T] {
}

class MyList1[+T] { //泛型的协变
}

class MyList2[-T] { //泛型的逆变
}
