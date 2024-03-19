package com.taotao.cloud.spark.atguigu.core.acc

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object Spark05_Bc {

  def main(args: Array[String]): Unit = {

    val sparConf = new SparkConf().setMaster("local").setAppName("Acc")
    val sc = new SparkContext(sparConf)

    val rdd1 = sc.makeRDD(List(
      ("a", 1), ("b", 2), ("c", 3)
    ))
    //        val rdd2 = sc.makeRDD(List(
    //            ("a", 4),("b", 5),("c", 6)
    //        ))
    val map = mutable.Map(("a", 4), ("b", 5), ("c", 6))



    // join会导致数据量几何增长，并且会影响shuffle的性能，不推荐使用
    //val joinRDD: RDD[(String, (Int, Int))] = rdd1.join(rdd2)
    //joinRDD.collect().foreach(println)
    // (a, 1),    (b, 2),    (c, 3)
    // (a, (1,4)),(b, (2,5)),(c, (3,6))
    rdd1.map {
      case (w, c) => {
        val l: Int = map.getOrElse(w, 0)
        (w, (c, l))
      }
    }.collect().foreach(println)


    sc.stop()

  }
}
