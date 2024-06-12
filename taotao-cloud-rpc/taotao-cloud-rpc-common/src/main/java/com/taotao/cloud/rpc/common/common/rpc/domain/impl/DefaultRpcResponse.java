package com.taotao.cloud.rpc.common.common.rpc.domain.impl;

import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;

/**
 * 默认 rpc 响应
 * @author shuigedeng
 * @since 2024.06
 */
public class DefaultRpcResponse implements RpcResponse {

    private static final long serialVersionUID = -2195142882293576847L;

    /**
     * 唯一标识
     * @since 2024.06
     */
    private String seqId;

    /**
     * 异常信息
     * @since 2024.06
     */
    private Throwable error;

    /**
     * 响应结果
     * @since 2024.06
     */
    private Object result;

    public static DefaultRpcResponse newInstance() {
        return new DefaultRpcResponse();
    }

    @Override
    public String seqId() {
        return seqId;
    }

    @Override
    public DefaultRpcResponse seqId(String seqId) {
        this.seqId = seqId;
        return this;
    }

    @Override
    public Throwable error() {
        return error;
    }

    public DefaultRpcResponse error(Throwable error) {
        this.error = error;
        return this;
    }

    @Override
    public Object result() {
        return result;
    }

    public DefaultRpcResponse result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRpcResponse{" +
                "seqId='" + seqId + '\'' +
                ", error=" + error +
                ", result=" + result +
                '}';
    }
}
