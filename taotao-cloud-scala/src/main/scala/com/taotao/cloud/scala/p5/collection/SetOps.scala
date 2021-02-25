package com.taotao.cloud.scala.p5.collection

import scala.collection.SortedSet

/**
  * scala集合set的操作
  */
object SetOps {
	def main(args: Array[String]): Unit = {
		val set = Set(2, 0, 1, 2, 3)
		println(set)
		println("--------有序的集合-----------")
		val sorted = SortedSet(//按照字符串的字典顺序来进行排序
			"abc",
			"abcd",
			"aac",
			"a",
			"abdc"
		)
		println(sorted)
		println("--------基于自定义数据构建有序的集合-----------")
		/*
		   自定义的比较，就必须要让元素具备可比性，或者为容器提供比较器
		   第一种，让元素具备比较性
		        java中，就是让该类型实现接口Comparable
		        scala中，就是让该类型实现特质Ordered
		        Ordered和Comparable啥关系
		        trait Ordered[T] extends Comparable[T] with Serializable
		   第一种，让容器具备比较器
		        Java中，就是让该容器实现接口Comparator
		        scala中，就是让该类型实现特质Ordering
		        Ordering和Comparator啥关系
		        trait Ordering[T] extends Comparator[T] with Serializable
		    需要注意：当比较器和比较性遇到一起，比较器优先
		 */
		val persons = SortedSet(
			Person("zhangsan", 18, 179.5),
			Person("lisi", 18, 178.5),
			Person("wangwu", 19, 180),
			Person("zhaoliu", 20, 168.5),
			Person("zhouqi", 19, 190.5),
			Person("weiba", 23, 180)
		)(new Ordering[Person](){
			/*
				定义比较规则：
					先按照身高的升序比较，身高相对，再按年龄降序排序
			 */
			override def compare(x: Person, y: Person) = {
				var ret = x.height.compareTo(y.height)
				if(ret == 0) {
					ret = y.age.compareTo(x.age)
				}
				ret
			}

		})
		persons.foreach(println)
	}
}
case class Person (name:String, age:Int, height:Double) extends Ordered[Person] {
	/*
		定义比较规则：
			先按照年龄的升序比较，年龄相对，再按身高降序排序
	 */
	override def compare(that: Person) = {
		var ret = this.age.compareTo(that.age)
		if(ret == 0) {
			ret = that.height.compareTo(this.height)
		}
		ret
	}
}
