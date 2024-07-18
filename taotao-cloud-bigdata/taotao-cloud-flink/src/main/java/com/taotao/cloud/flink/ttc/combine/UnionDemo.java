package com.taotao.cloud.flink.ttc.combine;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class UnionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Integer> source1 = env.fromElements(1, 2, 3);
        DataStreamSource<Integer> source2 = env.fromElements(11, 22, 33);
        DataStreamSource<String> source3 = env.fromElements("111", "222", "333");

        /**
         * TODO union：合并数据流
         * 1、 流的数据类型必须一致
         * 2、 一次可以合并多条流
         */
//        DataStream<Integer> union = source1.union(source2).union(source3.map(r -> Integer.valueOf(r)));
        DataStream<Integer> union = source1.union(source2, source3.map(r -> Integer.valueOf(r)));
        union.print();


        env.execute();
    }
}
