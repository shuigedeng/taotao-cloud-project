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

package com.taotao.cloud.order.application.stream.rocketmq;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSink;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    @StreamListener(TaoTaoCloudSink.EMAIL_MESSAGE_INPUT)
    public void onMessage(@Payload String message) {
        // LogUtils.info(
        //	"[onMessage][线程编号:{} 消息内容：{}]" + Thread.currentThread().getId() + message);
        LogUtils.info("email Consumer" + message);
    }

    // @StreamListener(MySink.TREK_INPUT)
    // public void onTrekMessage(@Payload Demo01Message message) {
    //    logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    // }
}
