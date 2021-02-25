package com.taotao.cloud.scala.p5.collection

import scala.collection.mutable
import scala.io.Source

/*
	使用scala来完成wordcount的统计
		要求使用集合和函数编程的思想
 */
object CollectionOps {
	def main(args: Array[String]): Unit = {
		val lines = Source.fromFile("data/hello.txt").getLines().toList

		lines.foreach(println)
		println("------------传统做法一：统计每一个单词的次数----------------")
		/*
			传统的做法：
				创建一个map容器，用来存放每一个单词出现的次数，key=word，value=count
		 */
		val map = mutable.Map[String, Int]()
		for(line <- lines) {
			val words = line.split("\\s+")
			for(word <- words) {
				val countOption = map.get(word)
				if(countOption.isDefined) {
					map.put(word, 1 + countOption.get)
				} else {
					map.put(word, 1 + 0)
				}
			}
		}
		for((word, count) <- map) {
			println(s"word: ${word}\tcount=${count}")
		}
		println("------------传统做法二：统计每一个单词的次数（优化版）----------------")
		val newMap = mutable.Map[String, Int]()
		for(line <- lines) {
			val words = line.split("\\s+")
			for(word <- words) {
				newMap.put(word, newMap.getOrElse(word, 0) + 1)
			}
		}
		for((word, count) <- newMap) {
			println(s"word: ${word}\tcount=${count}")
		}
		println("------------函数式做法一：统计每一个单词的次数----------------")

		val words = lines.flatMap(line => line.split("\\s+"))
		val word2Pairs:Map[String, List[String]] = words.groupBy(word => word)
		val word2Count = word2Pairs.map(t => (t._1, t._2.size))
		for((word, count) <- word2Count) {
			println(s"word: ${word}\tcount=${count}")
		}
		println("------------函数式做法二：统计每一个单词的次数(一气呵成)----------------")
		lines.flatMap(line => line.split("\\s+"))
			.groupBy(word => word)
		    .foreach{case (word, words) => {
			    println(s"word: ${word}\tcount=${words.size}")
		    }}
	}
}
