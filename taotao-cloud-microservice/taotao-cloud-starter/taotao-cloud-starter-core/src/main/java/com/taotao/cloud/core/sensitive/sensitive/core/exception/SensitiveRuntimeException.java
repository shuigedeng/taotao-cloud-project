package com.taotao.cloud.core.sensitive.sensitive.core.exception;

/**
 * 脱敏运行时异常
 */
public class SensitiveRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 6441791462011189106L;

    public SensitiveRuntimeException() {
    }

    public SensitiveRuntimeException(String message) {
        super(message);
    }

    public SensitiveRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SensitiveRuntimeException(Throwable cause) {
        super(cause);
    }

    public SensitiveRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
