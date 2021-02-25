package com.taotao.cloud.scala.p2.collection

/*
	scala不可变map的操作
 */
object ImmutableMapOps {
	def main(args: Array[String]): Unit = {
		val personMap = Map[String, Int](
			"张三" -> 13,
			"李四" -> 14,
			("old李" -> 18)
		)
		val key = "old李2"
		if(personMap.contains(key)) {
			//获取map中的元素
			val optionAge:Option[Int] = personMap.get(key)
			var age = optionAge.get
			println(age)
			//获取map中的元素方式二
			age = personMap(key)
			println(age)
		} else {
			//如果key存在，则获取对应的value，不存在，则返回该默认值
			val age = personMap.getOrElse(key, -1)
			println(age)
		}
		//修改 增加
//		personMap("王五") = 16
		//删除 drop删除返回一个新的集合，原集合不变，remove，原集合会改变
		println(personMap)
		println(personMap.drop(1))
		println(personMap)
	}
}
