package com.taotao.cloud.scala.p2.collection

import scala.collection.mutable

/*
scala可变map操作
 */
object MutableMapOps {
	def main(args: Array[String]): Unit = {
		val map = mutable.Map[String, Int](
			"张三" -> 13
		)
		println("原始map元素：" + map)
		//添加元素
		map("李四") = 14
		println(map)
		//修改
		map("李四") = 24
		println(map)
		map.put("王二麻子", 15)
		map.put("zl", 16)
		//删除操作
		println("map.drop(1): " + map.drop(1))//删除集合中的起始元素，并返回，元集合不变
		println(map)
		val value: Option[Int] = map.remove("zl")
		println(value.getOrElse(-1))
		println(map)
		///////////////////遍历///////////////////////
		val ks = map.keys
		for(key <- ks) {
			val value = map(key)
			println(key + "---->" + value)
		}
		println("scala的map遍历推荐方式一")
		for((k, v) <- map) {
			println(k + "---->" + v)
		}
		println("scala的map遍历推荐方式二")
		for(kv <- map) {
			println(kv._1 + "---->" + kv._2)
		}
		println("scala的map遍历推荐方式三")
		map.foreach(kv => {
			println(kv._1 + "---->" + kv._2)
		})
	}
}
