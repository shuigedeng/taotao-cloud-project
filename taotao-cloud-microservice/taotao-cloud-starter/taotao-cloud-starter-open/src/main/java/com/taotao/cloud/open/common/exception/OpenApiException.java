package com.taotao.cloud.open.common.exception;

/**
 * OpenApi异常
 *
 * @author wanghuidong
 */
public class OpenApiException extends RuntimeException {

    public OpenApiException(String errorMsg) {
        super(errorMsg);
    }

    public OpenApiException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
    }
}
