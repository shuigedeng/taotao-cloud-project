package com.taotao.cloud.redis.delay.exception;


public class MessageConversionException extends RuntimeException {

    public MessageConversionException() {

    }

    public MessageConversionException(String message) {
        super(message);
    }

    public MessageConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageConversionException(Throwable cause) {
        super(cause);
    }

    public MessageConversionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
