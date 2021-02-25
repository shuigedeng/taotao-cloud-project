package com.taotao.cloud.scala.p5.collection

/*
	list基本api的学习
 */
object ListOps2 {
	def main(args: Array[String]): Unit = {
		val left = List(1, 2, 3, 4)
		val right = List(4, 5, 6, 7, 8)
		//crud的操作
		//增
		/*
			增加一个元素：
				list.+:(A), 将元素A添加到集合list的首部，返回一个新的集合，原集合不变
				list.::(A), 将元素A添加到集合list的首部，返回一个新的集合，原集合不变
				list.:+(A), 将元素A添加到集合list的尾部，返回一个新的集合，原集合不变
			增加一个集合：
				list.:::(A), 将集合A添加到集合list的首部，返回一个新的集合，原集合不变
				list.++:(A), 将集合A添加到集合list的首部，返回一个新的集合，原集合不变
				list.++(A),  将集合A添加到集合list的尾部，返回一个新的集合，原集合不变
		 */
		var newList = left.+:(5)
		println("left.+:(5): " + newList)
		newList = left.::(5)
		println("left.::(5): " + newList)
		newList = left.:+(5)
		println("left.:+(5): " + newList)
		//添加集合
		newList = left.:::(right)
		println("left.:::(right): " + newList)
		newList = left.++:(right)
		println("left.++:(right): " + newList)
		newList = left.++(right)
		println("left.++(right): " + newList)
		//查询  list(index)
		val ret = newList(3)
		println("newList(3) = " + ret)
		/*修改
			Error:(39, 3) value update is not a member of List[Int]
			不可变集合，所以不能被修改
			newList(3) = -3
		 */
		//删除
		newList = newList.dropRight(1)
		println("newList.dropRight(1):  " + newList)
		//判断
		println("newList.contains(3): " + newList.contains(3))
		//遍历 略
		//其他一些操作
		//union 相当于sql中的union all
		newList = left.union(right)
		println("left.union(right):  " + newList)
		//intersect 交集
		newList = left.intersect(right)
		println("left.intersect(right):  " + newList)
		//diff 差集
		newList = left.diff(right)
		println("left.diff(right):  " + newList)
		//take
		val take2 = left.take(2)//获取集合中的前n个元素，如果该集合是有序的，那就是TopN
		println("left.take(2):  " + take2)
	}
}
