package com.taotao.cloud.rpc.common.common.rpc.domain;

/**
 * 序列化相关处理
 * @author shuigedeng
 * @since 2024.06
 */
public interface RpcResponse extends BaseRpc {

    /**
     * 异常信息
     * @return 异常信息
     * @since 2024.06
     */
    Throwable error();

    /**
     * 请求结果
     * @return 请求结果
     * @since 2024.06
     */
    Object result();

}
