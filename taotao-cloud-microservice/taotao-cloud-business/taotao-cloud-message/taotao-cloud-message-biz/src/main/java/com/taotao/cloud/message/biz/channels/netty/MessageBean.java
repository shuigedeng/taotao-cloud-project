package com.taotao.cloud.message.biz.channels.netty;

import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

/**
 * MessageBean
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBean {

    /**
     * 数据长度
     */
    private Integer len;
    /**
     * 通讯数据
     */
    private byte[] content;

    public MessageBean( Object object ) {
        content = JSONUtil.toJsonStr(object).getBytes(StandardCharsets.UTF_8);
        len = content.length;
    }
}

