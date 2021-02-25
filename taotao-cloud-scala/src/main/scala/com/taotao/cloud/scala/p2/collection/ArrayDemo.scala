package com.taotao.cloud.scala.p2.collection

/*
	scala集合体系之定长数组操作
 */
object ArrayDemo {
	def main(args: Array[String]): Unit = {
		//创建数组
		val array = new Array[Int](3)//这样，我们就创建了一个Int类型，长度为3的数组
		//使用数组Array的《伴生对象》来进行数组的创建
		val strArr = Array[String]("hello", "you", "me")
		//crud
		//修改数组指定索引对应的值
		strArr(1) = "shit"
		//获取数组指定索引的值
		println("index=1对应的数组的值：" + strArr(1))
		//判断
		if(strArr.contains("you")) {
			println("you在strArr中存在")
		} else {
			println("you在strArr中不存在")
		}
		//长度
		println("strArr.length == strArr.size ?" + (strArr.length == strArr.size))
		//遍历
		for(str <- strArr) {
			println(str)
		}
	}
}
