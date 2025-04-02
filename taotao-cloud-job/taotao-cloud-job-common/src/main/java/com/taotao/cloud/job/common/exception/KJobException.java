package com.taotao.cloud.job.common.exception;

public class KJobException extends RuntimeException{

    public KJobException() {
    }

    public KJobException(String message) {
        super(message);
    }

    public KJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public KJobException(Throwable cause) {
        super(cause);
    }

    public KJobException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
