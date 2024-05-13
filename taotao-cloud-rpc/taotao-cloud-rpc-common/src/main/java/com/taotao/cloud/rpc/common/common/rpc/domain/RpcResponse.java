package com.github.houbb.rpc.common.rpc.domain;

/**
 * 序列化相关处理
 * @author shuigedeng
 * @since 0.0.6
 */
public interface RpcResponse extends BaseRpc {

    /**
     * 异常信息
     * @return 异常信息
     * @since 0.0.6
     */
    Throwable error();

    /**
     * 请求结果
     * @return 请求结果
     * @since 0.0.6
     */
    Object result();

}
