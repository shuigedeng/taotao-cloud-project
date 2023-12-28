/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.sys.biz.pulsar.example.consumer;
//
// import com.taotao.cloud.sys.biz.pulsar.example.configuration.Topics;
// import com.taotao.cloud.sys.biz.pulsar.example.data.MyMsg;
// import io.github.majusko.pulsar.annotation.PulsarConsumer;
// import org.springframework.stereotype.Service;
//
// import java.util.concurrent.atomic.AtomicBoolean;
//
// @Service
// public class ConsumerService {
//	public AtomicBoolean stringReceived = new AtomicBoolean(false);
//	public AtomicBoolean classReceived = new AtomicBoolean(false);
//
//	@PulsarConsumer(topic = Topics.STRING, clazz = String.class)
//	public void consumeString(String message) {
//		LogUtils.info(message);
//		stringReceived.set(true);
//	}
//
//	@PulsarConsumer(topic = Topics.CLASS, clazz = MyMsg.class)
//	public void consumeClass(MyMsg message) {
//		LogUtils.info(message.getData());
//		classReceived.set(true);
//	}
// }
