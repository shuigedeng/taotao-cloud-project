package com.taotao.cloud.flink.doe.sources;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Date: 2023/12/27
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: nc  -lk 网络流数据   测试使用 单并行度
 */
public class Demo01Socket {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        /**
         * 主机名
         * 端口
         * + 数据的分割符 , 会对每行数据根据分隔符 切割  压平
         * + 最大连接重试次数
         */
        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899, ",", 3);
        ds.print();
        see.execute();


    }
}
