package com.taotao.cloud.bigdata.kafka.qianfeng;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 幂等性例子
 * Kafka生产数据保证消息一致性（exactly one 恰好一次）语义，靠开启幂等操作或者开启事务机制来完成
 * 下面案例中在同一个producer中，我们发送了两次重复的消息，
 * 看到的现象并没有像幂等操作那样，可以避免数据的重复。
 * 这是什么原因？
 *   其实主要原因在于大数据框架设计问题上
 *   如果说有1000W条数，在其中不断的进行增加，要保证添加的数据不能重复。
 *   方案一：在添加的时候，进行判断，如果该条数消息已经存在，直接覆盖掉对应数据
 *   方案二：在添加的时候，先不进行判断，直接进行添加，在后续的操作过程中满足条件之后
 *   在进行数据的合并操作。
 *   我们选择方案一还是方案二？
 *   在这里我们肯定选择方案二，为什么，原因在于添加一条数据就要扫描一次，判断存不存在，存在就
 *   覆盖，这样会很严重的影响写入的性能，如果我们进行判断，后期在进行聚合去重，对写入没有任何影响。
 *
 *  面试题：如何保证kafka的数据一致性？
 *  答：Kafka生产者可以选择生产的两种模式（幂等和事务）
 *  幂等：多次操作的结果和操作一次的结果是一样的
 *  事务：发送数据在一个事务中，如果有异常，将会回滚
 */
class _02ExactlyOneProducerTest {
    public static void main(String[] args) throws Exception{
        // 加载配置信息
        Properties prop = new Properties();
        prop.load(_01MyProducerTest.class
                .getClassLoader().getResourceAsStream("producer.properties"));
        String topic = "spark";
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);
        // 加载Topic
        ProducerRecord<String, String> record = null;
        int start = 6;
        int end = start + 10;
        for (int i= start;i<end; i++){ // 每次发送十条记录
            record =new ProducerRecord<String, String>(topic,i+"",i+"");
            producer.send(record);
        }
        // 第二遍
        for (int i= start;i<end; i++){ // 每次发送十条记录
            record =new ProducerRecord<String, String>(topic,i+"",i+"");
            producer.send(record);
        }
        // 释放资源
        producer.close();
    }
}
