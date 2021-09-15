package com.taotao.cloud.scala.atguigu.chapter07

object Test17_CommonWordCount {
  def main(args: Array[String]): Unit = {
    val stringList: List[String] = List(
      "hello",
      "hello world",
      "hello scala",
      "hello spark from scala",
      "hello flink from scala"
    )

    // 1. 对字符串进行切分，得到一个打散所有单词的列表
    //    val wordList1: List[Array[String]] = stringList.map(_.split(" "))
    //    val wordList2: List[String] = wordList1.flatten
    //    println(wordList2)
    val wordList = stringList.flatMap(_.split(" "))
    println(wordList)

    // 2. 相同的单词进行分组
    val groupMap: Map[String, List[String]] = wordList.groupBy(word => word)
    println(groupMap)

    // 3. 对分组之后的list取长度，得到每个单词的个数
    val countMap: Map[String, Int] = groupMap.map(kv => (kv._1, kv._2.length))

    // 4. 将map转换为list，并排序取前3
    val sortList: List[(String, Int)] = countMap.toList
      .sortWith(_._2 > _._2)
      .take(3)

    println(sortList)
  }
}
