package com.taotao.cloud.message.biz.channels.netty;

import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ChatMsg
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg implements Serializable {

    private String senderId;
    private String receiverId;
    private String msg;
    private String msgId;
}

