package com.taotao.cloud.data.sync.kafka.qianfeng;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Kafka自动提交偏移量操作 这种自动提交offset是会周期性的进行offset提交，如果收该周期设置的视角比较大，就有可能
 * 造成数据的读取重复，所以我们如果使用这种提交方式的话，那么应该尽可能把时间设置短一点
 * <p>
 * 这种方式有一点问题，因为自动管理是周期性提交方式，那么如果在这种个周期提交的时候，正好挂了 此时这个周期的offset就没交上去，那么就丢失了，造成数据不准，这种问题如何解决？
 * 就需要我们自己手动管理offset了。
 * <p>
 * kafka手动维护Offset 注释：手动管理Offset会在SparkStreaming中重点讲解，这里简单介绍如何管理即可
 * 用户手动管理Offset的话，我们需要提取Kafka的Offset，每次消费数据的时候，拿到Offset
 * 然后将这个Offset存入一个系统或者数据库都行，就将它保存起来，当我们数据处理完成后，在 往系统或者数据库提交此Offset，这样offset在我们自己的系统或者数据库中，我们就不用担心
 * 周期性提交失败问题。
 */
public class _03AutoOffsetCommitConsumerOps {

	public static void main(String[] args) throws Exception {
		try {
			Properties prop = new Properties();
			prop.load(
				_01MyProducerTest.class.getClassLoader()
					.getResourceAsStream("consumer.properties"));
			// 构建消费者
			KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(prop);
			// 消费对应的Topic数据
			consumer.subscribe(Arrays.asList("spark"));
			// 打印所有相关的数据信息
			System.out.println("topic\tpartition\toffset\tkey\tvalue");
			while (true) {
                /*
                 消费数据 timeout:从consumer的缓冲区Buffer中获取可用数据的等待超时时间
                 如果设置为0，则会理解返回该缓冲区内的所有数据，如果不设置为零，返回空，并且不能写负数
                 */
				ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
				for (ConsumerRecord<String, String> cr : consumerRecords) {
					String topic = cr.topic();
					int partition = cr.partition();
					String key = cr.key();
					String value = cr.value();
					long offset = cr.offset();
					System.out.printf(
						"topic:%s\tpartition:%d\toffset:%d\tkey:%s\tvalue:%s\r\n",
						topic, partition, offset, key, value);
				}
			}
			// consumer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
