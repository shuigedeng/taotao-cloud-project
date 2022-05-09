package com.taotao.cloud.health.alarm.core.exception;

public class DuplicatedAlarmExecuteDefinedException extends RuntimeException {

    public DuplicatedAlarmExecuteDefinedException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
