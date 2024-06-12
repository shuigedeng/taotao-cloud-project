package com.taotao.cloud.rpc.registry.register.domain.message;


import com.taotao.cloud.rpc.common.common.rpc.domain.BaseRpc;

/**
 * 注册消息体
 * @author shuigedeng
 * @since 2024.06
 */
public interface NotifyMessage extends BaseRpc {

    /**
     * 头信息
     * @return 头信息
     * @since 2024.06
     */
    NotifyMessageHeader header();

    /**
     * 消息信息体
     * @return 消息信息体
     * @since 2024.06
     */
    Object body();

}
