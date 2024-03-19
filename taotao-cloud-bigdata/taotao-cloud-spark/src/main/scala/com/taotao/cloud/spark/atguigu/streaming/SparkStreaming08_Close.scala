package com.taotao.cloud.spark.atguigu.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}

object SparkStreaming08_Close {

    def main(args: Array[String]): Unit = {

        /*
           线程的关闭：
           val thread = new Thread()
           thread.start()

           thread.stop(); // 强制关闭

         */

        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
        val ssc = new StreamingContext(sparkConf, Seconds(3))

        val lines = ssc.socketTextStream("localhost", 9999)
        val wordToOne = lines.map((_,1))

        wordToOne.print()

        ssc.start()

        // 如果想要关闭采集器，那么需要创建新的线程
        // 而且需要在第三方程序中增加关闭状态
        new Thread(
            new Runnable {
                override def run(): Unit = {
                    // 优雅地关闭
                    // 计算节点不在接收新的数据，而是将现有的数据处理完毕，然后关闭
                    // Mysql : Table(stopSpark) => Row => data
                    // Redis : Data（K-V）
                    // ZK    : /stopSpark
                    // HDFS  : /stopSpark
                    /*
                    while ( true ) {
                        if (true) {
                            // 获取SparkStreaming状态
                            val state: StreamingContextState = ssc.getState()
                            if ( state == StreamingContextState.ACTIVE ) {
                                ssc.stop(true, true)
                            }
                        }
                        Thread.sleep(5000)
                    }
                     */

                    Thread.sleep(5000)
                    val state: StreamingContextState = ssc.getState()
                    if ( state == StreamingContextState.ACTIVE ) {
                        ssc.stop(true, true)
                    }
                    System.exit(0)
                }
            }
        ).start()

        ssc.awaitTermination() // block 阻塞main线程


    }

}
