package com.taotao.cloud.data.elasticsearch.esearchx.exception;

/**
 * 请求异常
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:05:36
 */
public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }
}
