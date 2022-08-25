package com.taotao.cloud.schedule.exception;

/**
 * ScheduledException
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 16:03:31
 */
public class ScheduledException extends RuntimeException {
    public ScheduledException() {
        super("未知错误");
    }

    public ScheduledException(String message) {
        super(message);
    }

    public ScheduledException(String message, Throwable cause) {
        super(message, cause);
    }
}
