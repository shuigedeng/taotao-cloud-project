package com.taotao.cloud.openapi.common.exception;

/**
 * OpenApi异常
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:06:21
 */
public class OpenApiException extends RuntimeException {

    public OpenApiException(String errorMsg) {
        super(errorMsg);
    }

    public OpenApiException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
    }
}
