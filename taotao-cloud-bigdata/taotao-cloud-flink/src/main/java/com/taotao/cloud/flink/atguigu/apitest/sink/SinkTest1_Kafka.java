package com.taotao.cloud.flink.atguigu.apitest.sink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public class SinkTest1_Kafka {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

//        // 从文件读取数据
//        DataStream<String> inputStream = env.readTextFile("D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\sensor.txt");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset", "latest");

        // // 从文件读取数据
        // DataStream<String> inputStream = env.addSource( new FlinkKafkaConsumer011<String>("sensor", new SimpleStringSchema(), properties));
		//
        // // 转换成SensorReading类型
        // DataStream<String> dataStream = inputStream.map(line -> {
        //     String[] fields = line.split(",");
        //     return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2])).toString();
        // });
		//
        // dataStream.addSink( new FlinkKafkaProducer011<String>("localhost:9092", "sinktest", new SimpleStringSchema()));
		//
        // env.execute();
    }
}
