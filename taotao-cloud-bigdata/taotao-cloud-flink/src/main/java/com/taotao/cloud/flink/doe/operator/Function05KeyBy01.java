package com.taotao.cloud.flink.doe.operator;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Date: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Function05KeyBy01 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(4);
        // 禁用算子合并成算子链
        see.disableOperatorChaining();

        DataStreamSource<String> dataStreamSource = see.socketTextStream("doe01", 8899);


        SingleOutputStreamOperator<String> words = dataStreamSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {

                for (String word : value.split("\\s+")) {
                    out.collect(word);
                }
            }
        });
                /**
                 * 分组
                 */
        KeyedStream<String, String> keyed = words.keyBy(new KeySelector<String, String>() {
            @Override
            public String getKey(String value) throws Exception {
                return value+"_"+1;
            }
        });

        SingleOutputStreamOperator<String> map2 = keyed.map(e -> e);

        map2.print();
        see.execute();


    }
}
