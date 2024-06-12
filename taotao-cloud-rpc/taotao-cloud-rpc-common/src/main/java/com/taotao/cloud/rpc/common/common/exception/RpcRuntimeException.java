package com.taotao.cloud.rpc.common.common.exception;

/**
 * rpc 运行时异常
 * @author shuigedeng
 * @since 2024.06
 */
public class RpcRuntimeException extends RuntimeException{

    private static final long serialVersionUID = -2521814477982789832L;

    public RpcRuntimeException() {
    }

    public RpcRuntimeException(String message) {
        super(message);
    }

    public RpcRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcRuntimeException(Throwable cause) {
        super(cause);
    }

    public RpcRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
