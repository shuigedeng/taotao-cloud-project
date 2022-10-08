package com.taotao.cloud.lock.kylin.exception;

/**
 * 获取锁失败异常
 *
 * @author wangjinkui
 */
public class LockFailureException extends LockException {

    public LockFailureException() {
        super();
    }

    public LockFailureException(String message) {
        super(message);
    }
}
