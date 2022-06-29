package com.taotao.cloud.order.biz;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSink;
import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSource;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED
 * --add-opens java.base/java.util=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
 * --add-opens java.base/java.math=ALL-UNNAMED
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:48:21
 */
@EnableBinding({TaoTaoCloudSink.class, TaoTaoCloudSource.class})
@TaoTaoCloudApplication
public class TaoTaoCloudOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudOrderApplication.class, args);
	}

	@Bean
	public TopicExchange topicExchange001() {
		return new TopicExchange("sms-topic", true, false);
	}

}
