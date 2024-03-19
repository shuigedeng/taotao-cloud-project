package com.taotao.cloud.flink.doe.sink;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Date: 2023/12/30
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Sink01Print {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        // 数据的上下游分发规则是  均衡  先随机获取一个SubtaskID , 依次累加SubtaskID分发数据 ,做到数据轮循式均匀分配
        SingleOutputStreamOperator<String> ds2 = ds.map(String::toUpperCase);

        ds2.keyBy(e->"") ;/*.max("") ;*/
        //输出
        // 多并行输出
        //  ds2.print() ;
        ds2.print("网络流"); // 执行输出名称


        see.execute("my_Job");


    }
}
