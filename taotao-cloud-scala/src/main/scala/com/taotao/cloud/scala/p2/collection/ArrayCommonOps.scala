package com.taotao.cloud.scala.p2.collection

/*
scala 数组通用操作
 */
object ArrayCommonOps {
	def main(args: Array[String]): Unit = {
		//使用了数组的伴生对象创建了一个Array实例
		var array = Array(-1, -2, -3, -4, -5, -6)

		//定长数组---->可变数组之间的转换
		//array--->arrayBuffer
		val ab = array.toBuffer
		println(ab)
		//arrayBuffer--->array
		array = ab.toArray
		//mkString,将数组/集合，转化为自定义格式字符串
		println(array.mkString)//就是将集合中每一个元素拼接成一个字符串
		println(array.mkString(","))//就是将集合中每一个元素按照分隔符,拼接成一个字符串
		println(array.mkString("[", ", ", "]"))//就是将集合中每一个元素按照分隔符,拼接成一个字符串,同时添加起始和结束字符
		//遍历数组
		println("普通的数组变量方式")
		for(i <- ab) {
			println(i)
		}
		println("高级的数组变量方式---foreach函数")
		array.foreach(num => println(num))
		//求和
		println("========数组求和============")
		var sum = 0
		for(i <- ab) {
			sum += i
		}
		println(sum == array.sum)
		//最大值
		println("========数组最值============")
		var max = array(0)
		var min = array(0)
		for(i <- 1 until array.length) {
			if(max < array(i)) {
				max = array(i)
			}
			if(min > array(i)) {
				min = array(i)
			}
		}
		println("数组array中的最大值：" + max + ", scala提供的遍历操作：" + array.max)
		println("数组array中的最小值：" + min + ", scala提供的遍历操作：" + array.min)

		//排序
		var sortedArray = array.sorted
		println("排序之后的集合：" + sortedArray.mkString("[", ", ", "]"))
		sortedArray = sortedArray.sortWith((v1, v2) => v1 > v2)
		println("排序之后的集合：" + sortedArray.mkString("[", ", ", "]"))
	}
}
