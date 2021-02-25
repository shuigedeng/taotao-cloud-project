/**
 * Project Name: projects
 * Package Name: com.taotao.cloud.demo.graphql
 * Date: 2020/9/23 16:48
 * Author: dengtao
 */
package com.taotao.cloud.demo.graphql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * <br>
 *
 * @author dengtao
 * @version v1.0
 * @date 2020/9/23 16:48
 */
@Component
@Slf4j
public class KafkaSend implements CommandLineRunner {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String data) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, data);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送消息失败："+ ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {

            }
        });
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 10; i++) {
            sendMessage("taotao-cloud-sys-log", "hello");
        }
    }
}
