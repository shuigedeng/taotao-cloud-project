package com.taotao.cloud.flink.doe.operator;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Date: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 * 接收数据
 *      将接收的数据组织成Bean
 *      lambda表达式实现map算子中的计算逻辑
 *      如果接口中只有一个方法可以使用 lambda
 */
public class Function01Map05 {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
      //Lambda
        // 单并行逐个接收
        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);

        // 简单的数据处理
        SingleOutputStreamOperator<String> res = ds.map(line -> {
            return line.toUpperCase();
        });
        ds.map(String::toUpperCase) ;

        res.print();
        see.execute()  ;


    }
}
