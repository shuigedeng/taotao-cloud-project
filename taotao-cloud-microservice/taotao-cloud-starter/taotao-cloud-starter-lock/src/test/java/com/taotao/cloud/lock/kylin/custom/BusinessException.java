package com.taotao.cloud.lock.kylin.custom;

/**
 * @author wangjinkui
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}
