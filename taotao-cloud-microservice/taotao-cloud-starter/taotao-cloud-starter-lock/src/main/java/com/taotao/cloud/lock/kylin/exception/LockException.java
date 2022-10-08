package com.taotao.cloud.lock.kylin.exception;

/**
 * 锁异常
 *
 * @author wangjinkui
 */
public class LockException extends RuntimeException {

    public LockException() {
        super();
    }

    public LockException(String message) {
        super(message);
    }

}
