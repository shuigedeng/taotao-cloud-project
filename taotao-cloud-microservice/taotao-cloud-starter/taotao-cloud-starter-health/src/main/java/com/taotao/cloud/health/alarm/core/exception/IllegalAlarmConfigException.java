package com.taotao.cloud.health.alarm.core.exception;

public class IllegalAlarmConfigException extends RuntimeException {

    public IllegalAlarmConfigException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
