package com.taotao.cloud.limit.ratelimiter;

public class ExecuteFunctionException extends RuntimeException {

    public ExecuteFunctionException(String message, Throwable cause) {
        super(message, cause);
    }
}
