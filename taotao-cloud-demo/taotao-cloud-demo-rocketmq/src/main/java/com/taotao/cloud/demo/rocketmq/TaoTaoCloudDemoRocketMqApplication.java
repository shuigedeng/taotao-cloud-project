/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.demo.rocketmq;

import com.taotao.cloud.demo.rocketmq.producer.model.Order;
import com.taotao.cloud.demo.rocketmq.producer.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Rocketmq生产者 demo
 */
@SpringBootApplication
public class TaoTaoCloudDemoRocketMqApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudDemoRocketMqApplication.class, args);
	}

	@Bean
	public CustomRunner customRunner() {
		return new CustomRunner();
	}

	/**
	 * 工程启动后执行 共发送5条消息：2条为字符消息，3条为带tag的对象消息
	 */
	public static class CustomRunner implements CommandLineRunner {

		@Autowired
		private SenderService senderService;

		@Override
		public void run(String... args) {
			int count = 5;
			for (int index = 1; index <= count; index++) {
				String msgContent = "msg-" + index;
				if (index % 2 == 0) {
					senderService.send(msgContent);
				} else {
					senderService.sendWithTags(new Order((long) index, "order-" + index), "tagObj");
				}
			}
		}
	}
}
