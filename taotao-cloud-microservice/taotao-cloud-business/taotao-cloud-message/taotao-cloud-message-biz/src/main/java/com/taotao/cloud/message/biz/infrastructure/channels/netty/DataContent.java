package com.taotao.cloud.message.biz.infrastructure.channels.netty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * DataContent
 *
 * @author shuigedeng
 * @version 2026.04
 * @since 2025-12-19 09:30:45
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * 由于我们这边Nutty处理的消息只有注册，所以话这里只需要
 * 保留action和userid即可
 * */
public class DataContent implements Serializable {

    private Integer action;
    private String userid;
}
