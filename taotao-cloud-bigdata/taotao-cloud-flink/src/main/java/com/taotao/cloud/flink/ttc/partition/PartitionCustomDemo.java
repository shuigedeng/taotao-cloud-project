package com.taotao.cloud.flink.ttc.partition;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class PartitionCustomDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(2);

        DataStreamSource<String> socketDS = env.socketTextStream("hadoop102", 7777);

        socketDS
                .partitionCustom(new MyPartitioner(), r->r)
                .print();



        env.execute();
    }
}
