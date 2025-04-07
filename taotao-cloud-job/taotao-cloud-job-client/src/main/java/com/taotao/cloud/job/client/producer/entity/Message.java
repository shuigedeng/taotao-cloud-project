package com.taotao.cloud.job.client.producer.entity;

import com.taotao.cloud.job.common.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Message {
	MessageType type;
	byte[] body;
}
