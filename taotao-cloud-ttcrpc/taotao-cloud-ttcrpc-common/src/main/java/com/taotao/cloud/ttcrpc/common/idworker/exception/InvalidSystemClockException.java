package com.taotao.cloud.ttcrpc.common.idworker.exception;

/**
 * 时钟回拨异常
 */
public class InvalidSystemClockException extends RuntimeException {
    public InvalidSystemClockException(String message) {
        super(message);
    }
}
