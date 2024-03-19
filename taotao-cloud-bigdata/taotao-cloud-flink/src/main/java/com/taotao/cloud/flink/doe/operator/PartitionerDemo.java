package com.taotao.cloud.flink.doe.operator;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Date: 2023/12/31
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class PartitionerDemo {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(4) ;
        see.disableOperatorChaining() ;

        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        SingleOutputStreamOperator<String> ds1 = ds.map(String::toUpperCase).setParallelism(4);

        // DataStream<String> ds2 = ds1.forward();
      //  DataStream<String> ds2 = ds1.broadcast();
        //  DataStream<String> ds2 = ds1.shuffle() ;
         // DataStream<String> ds2 = ds1.rebalance() ;
          DataStream<String> ds2 = ds1.global() ;


        ds2.print()  ;
        see.execute() ;
    /*    ds2.print()  ;
        see.execute() ;
        ds1.broadcast() ;
        ds1.shuffle() ;
        ds1.rebalance() ;
        ds1.global() ;
        ds1.rescale() ;*/


    }
}
