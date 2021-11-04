package com.taotao.cloud.order.biz;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSink;
import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSource;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED
 * --add-opens java.base/java.util=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
 * --add-opens java.base/java.math=ALL-UNNAMED
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-12 10:45:43
 */
@EnableBinding({TaoTaoCloudSink.class, TaoTaoCloudSource.class})
@TaoTaoCloudApplication
@MapperScan(basePackages = "com.taotao.cloud.order.biz.mapper")
@EnableJpaRepositories(basePackages = "com.taotao.cloud.order.biz.repository.inf")
public class TaoTaoCloudOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudOrderApplication.class, args);
	}

}
