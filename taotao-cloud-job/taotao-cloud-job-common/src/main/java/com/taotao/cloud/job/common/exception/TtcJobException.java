package com.taotao.cloud.job.common.exception;

public class TtcJobException extends RuntimeException{

    public TtcJobException() {
    }

    public TtcJobException(String message) {
        super(message);
    }

    public TtcJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public TtcJobException(Throwable cause) {
        super(cause);
    }

    public TtcJobException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
