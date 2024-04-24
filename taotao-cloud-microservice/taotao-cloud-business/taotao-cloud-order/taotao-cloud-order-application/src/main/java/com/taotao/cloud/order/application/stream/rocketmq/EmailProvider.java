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

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmailProvider {

    @Autowired
    private TaoTaoCloudSource source;

    // 普通发送
    public void send(String content) {
        Message<String> msg = MessageBuilder.withPayload(content).build();
        source.emailOutput().send(MessageBuilder.withPayload(content).build());
    }

    //// 顺序发送
    // public void sendOrderly(String content,String tag) {
    //    MsgModel msgModel = new MsgModel(content,System.currentTimeMillis());
    //    Message<MsgModel> msg = MessageBuilder.withPayload(msgModel)
    //            .setHeader(MessageConst.PROPERTY_TAGS,tag)
    //            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
    //            .build();
    //    msgSource.orderlyMsg().send(msg);
    // }
    //
    //// 事务消息 half 消息，这条消息去到MQ 在本地事务没有执行成功之前，对于消费者来说是不可见，无法消费的，这里的事务操作是指确保
    //// 本地事务执行成功之后，消息发送到MQ并可以被消费
    //// 对于消费者来说，只要消费MQ的MQ信息就好，但是要确保，事务消息的消费结果是幂等性的，即多次消费结果都一样，MQ不确保消息被消费一次
    // public void sendTransaction(String content,int status){
    //    MsgModel msgModel = new MsgModel(content,System.currentTimeMillis());
    //    Message<MsgModel> msg = MessageBuilder.withPayload(msgModel)
    //            .setHeader("status",status)
    //            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
    //            .build();
    //    msgSource.transactionMsg().send(msg);
    // }
}
