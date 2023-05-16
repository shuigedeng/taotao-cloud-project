package com.taotao.cloud.message.biz.channels.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg implements Serializable {
    private String senderId;
    private String receiverId;
    private String msg;
    private String msgId;
}

