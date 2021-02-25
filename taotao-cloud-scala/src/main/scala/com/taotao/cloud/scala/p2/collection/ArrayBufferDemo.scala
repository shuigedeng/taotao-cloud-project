package com.taotao.cloud.scala.p2.collection

import scala.collection.mutable.ArrayBuffer

/*
	scala中的可变数组ArrayBuffer
 */
object ArrayBufferDemo {
	def main(args: Array[String]): Unit = {
		//可变数组的构建
		val arraybuffer = new ArrayBuffer[Int](3)// 3是可变数组内部维护的定长数组的初始化长度，当达到这个阈值的时候，进行扩容
		var strArr = new ArrayBuffer[String]()
		//使用ArrayBuffer的伴生对象来创建对象，创建了一个String的ArrayBuffer，有两个初始化的元素
		strArr = ArrayBuffer[String]("hello", "you")
		////////////////crud///////////////////////
		println(arraybuffer)
		println("增加-------------")
		arraybuffer.append(1)
		println(arraybuffer)
		arraybuffer.append(2, 3, 4)
		println(arraybuffer)
		println("插入-------------")
		arraybuffer.insert(1, -1, -2)//在指定的索引位置上面插入n个元素
		println(arraybuffer)
		println("获取--------------")
		println(arraybuffer(3))//获取制定索引位置上的元素
		println("修改--------------")
		arraybuffer(4) = -4
		println(arraybuffer(4))
		println("删除--------------")
		val ret = arraybuffer.remove(2)//删除并获取制定索引位置上的元素
		println("arraybuffer.remove(2): " + ret)
		println(arraybuffer)
		////drop
		val newAb = arraybuffer.drop(2)//删除集合前n个元素，将剩余元素组成一个新的集合，而原集合不变
		println("arraybuffer.drop(2)之后的ab: " + arraybuffer)
		println("arraybuffer.drop(2)的返回值：" + newAb)
		//删除多个元素
		arraybuffer.remove(1, 3)//从指定索引位置开始删除指定个数个个元素
		println("arraybuffer.remove(1, 3): " + arraybuffer)
		//判断
		if(arraybuffer.contains(-4)) {
			println("-4在strArr中存在")
		} else {
			println("-4在strArr中不存在")
		}
		//长度
		println("arraybuffer.length == arraybuffer.size ?" + (arraybuffer.length == arraybuffer.size))
	}
}
