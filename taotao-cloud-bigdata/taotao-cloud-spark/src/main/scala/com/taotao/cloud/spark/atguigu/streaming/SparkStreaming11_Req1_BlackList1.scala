package com.taotao.cloud.spark.atguigu.streaming

import com.taotao.cloud.bigdata.spark.atguigu.util.JDBCUtil

import java.sql.ResultSet
import java.text.SimpleDateFormat
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer

object SparkStreaming11_Req1_BlackList1 {

    def main(args: Array[String]): Unit = {

        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
        val ssc = new StreamingContext(sparkConf, Seconds(3))

        val kafkaPara: Map[String, Object] = Map[String, Object](
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "linux1:9092,linux2:9092,linux3:9092",
            ConsumerConfig.GROUP_ID_CONFIG -> "atguigu",
            "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
            "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
        )

        val kafkaDataDS: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
            ssc,
            LocationStrategies.PreferConsistent,
            ConsumerStrategies.Subscribe[String, String](Set("atguiguNew"), kafkaPara)
        )
        val adClickData = kafkaDataDS.map(
            kafkaData => {
                val data = kafkaData.value()
                val datas = data.split(" ")
                AdClickData(datas(0),datas(1),datas(2),datas(3),datas(4))
            }
        )

        val ds = adClickData.transform(
            rdd => {
                // TODO 通过JDBC周期性获取黑名单数据
                val blackList = ListBuffer[String]()

                val conn = JDBCUtil.getConnection
                val pstat = conn.prepareStatement("select userid from black_list")

                val rs: ResultSet = pstat.executeQuery()
                while ( rs.next() ) {
                    blackList.append(rs.getString(1))
                }

                rs.close()
                pstat.close()
                conn.close()

                // TODO 判断点击用户是否在黑名单中
                val filterRDD = rdd.filter(
                    data => {
                        !blackList.contains(data.user)
                    }
                )

                // TODO 如果用户不在黑名单中，那么进行统计数量（每个采集周期）
                filterRDD.map(
                    data => {
                        val sdf = new SimpleDateFormat("yyyy-MM-dd")
                        val day = sdf.format(new java.util.Date( data.ts.toLong ))
                        val user = data.user
                        val ad = data.ad

                        (( day, user, ad ), 1) // (word, count)
                    }
                ).reduceByKey(_+_)
            }
        )

        ds.foreachRDD(
            rdd => {
                // rdd. foreach方法会每一条数据创建连接
                // foreach方法是RDD的算子，算子之外的代码是在Driver端执行，算子内的代码是在Executor端执行
                // 这样就会涉及闭包操作，Driver端的数据就需要传递到Executor端，需要将数据进行序列化
                // 数据库的连接对象是不能序列化的。

                // RDD提供了一个算子可以有效提升效率 : foreachPartition
                // 可以一个分区创建一个连接对象，这样可以大幅度减少连接对象的数量，提升效率
                rdd.foreachPartition(iter => {
                        val conn = JDBCUtil.getConnection
                        iter.foreach{
                            case ( ( day, user, ad ), count ) => {

                            }
                        }
                        conn.close()
                    }
                )

                rdd.foreach{
                    case ( ( day, user, ad ), count ) => {
                        println(s"${day} ${user} ${ad} ${count}")
                        if ( count >= 30 ) {
                            // TODO 如果统计数量超过点击阈值(30)，那么将用户拉入到黑名单
                            val conn = JDBCUtil.getConnection
                            val sql = """
                                        |insert into black_list (userid) values (?)
                                        |on DUPLICATE KEY
                                        |UPDATE userid = ?
                                      """.stripMargin
                            JDBCUtil.executeUpdate(conn, sql, Array( user, user ))
                            conn.close()
                        } else {
                            // TODO 如果没有超过阈值，那么需要将当天的广告点击数量进行更新。
                            val conn = JDBCUtil.getConnection
                            val sql = """
                                        | select
                                        |     *
                                        | from user_ad_count
                                        | where dt = ? and userid = ? and adid = ?
                                      """.stripMargin
                            val flg = JDBCUtil.isExist(conn, sql, Array( day, user, ad ))

                            // 查询统计表数据
                            if ( flg ) {
                                // 如果存在数据，那么更新
                                val sql1 = """
                                             | update user_ad_count
                                             | set count = count + ?
                                             | where dt = ? and userid = ? and adid = ?
                                           """.stripMargin
                                JDBCUtil.executeUpdate(conn, sql1, Array(count, day, user, ad))
                                // TODO 判断更新后的点击数据是否超过阈值，如果超过，那么将用户拉入到黑名单。
                                val sql2 = """
                                             |select
                                             |    *
                                             |from user_ad_count
                                             |where dt = ? and userid = ? and adid = ? and count >= 30
                                           """.stripMargin
                                val flg1 = JDBCUtil.isExist(conn, sql2, Array( day, user, ad ))
                                if ( flg1 ) {
                                    val sql3 = """
                                                |insert into black_list (userid) values (?)
                                                |on DUPLICATE KEY
                                                |UPDATE userid = ?
                                              """.stripMargin
                                    JDBCUtil.executeUpdate(conn, sql3, Array( user, user ))
                                }
                            } else {
                                val sql4 = """
                                             | insert into user_ad_count ( dt, userid, adid, count ) values ( ?, ?, ?, ? )
                                           """.stripMargin
                                JDBCUtil.executeUpdate(conn, sql4, Array( day, user, ad, count ))
                            }
                            conn.close()
                        }
                    }
                }
            }
        )

        ssc.start()
        ssc.awaitTermination()
    }
    // 广告点击数据
    case class AdClickData( ts:String, area:String, city:String, user:String, ad:String )

}
