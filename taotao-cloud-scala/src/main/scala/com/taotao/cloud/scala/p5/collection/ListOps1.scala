package com.taotao.cloud.scala.p5.collection

/*
	scala中的列表操作：
	scala中的列表List，首先是Seq的子trait，其要么使用空列表(Nil)，要么就是又一个head头元素，和其余元素组成的tail子列表来构成。
	其中head、tail，以及isEmpty是List列表的基本操作
 */
object ListOps1 {
	def main(args: Array[String]): Unit = {
		val list = List(3, -6, 7, 1, 0, 9, 4, 2)
		//head
		val headEle = list.head
		println("head: " + headEle)
		//tail
		val tailList = list.tail
		println(tailList.mkString("[", ", ", "]"))
		//isEmpty
		val isNull = list.isEmpty
		println("isNull: " + isNull)
		val newList = Nil //构建了一个空列表

		//使用递归思想，来完成list元素的求和
		val sum = getTotal(list)
		println("sum: " + sum)
	}
	def getTotal(list: List[Int]):Int = {
//		if(list.isEmpty) {
//			0
//		} else {
//			list.head + getTotal(list.tail)
//		}
		list match {
			case Nil => 0
			case array => array.head + getTotal(array.tail)
		}
	}
}
