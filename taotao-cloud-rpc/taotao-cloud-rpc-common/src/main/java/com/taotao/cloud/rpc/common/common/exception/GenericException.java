package com.taotao.cloud.rpc.common.common.exception;

/**
 * 泛化异常
 * @author shuigedeng
 * @since 0.1.2
 */
public class GenericException extends RuntimeException{

    public GenericException() {
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericException(Throwable cause) {
        super(cause);
    }

    public GenericException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
