/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.response.respcode;


import java.io.Serializable;

/**
 * 响应码接口定义
 *
 * @author bbhou
 * @since 0.0.1
 */
public interface RespCode extends Serializable {

    /**
     * 错误编码
     * 1. 对于所有的错误码都应该按照约定，便于区分，尽量按照大家都公认的方向进行设计。
     * @return 错误编码
     */
    String getCode();

    /**
     * 错误消息
     * 1. 对于错误的描述，简明扼要。可以直接展现给客户看。
     * @return 错误消息
     */
    String getMsg();

}
