package com.taotao.cloud.job.client.producer.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.kjob.common.enums.MessageType;

@AllArgsConstructor
@NoArgsConstructor
public class Message {
    MessageType type;
    byte[] body;
}
