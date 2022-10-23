package com.taotao.cloud.elasticsearch.esearchx.exception;

/**
 * 不存在异常
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:05:32
 */
public class NoExistException extends RuntimeException {
    public NoExistException(String message) {
        super(message);
    }
}
