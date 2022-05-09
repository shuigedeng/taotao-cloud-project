package com.taotao.cloud.health.alarm.core.exception;

public class RegisterInfoIllegalException extends RuntimeException {
    public RegisterInfoIllegalException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
