package com.taotao.cloud.openapi.common.exception;

/**
 * OpenApi客户端异常
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:06:19
 */
public class OpenApiClientException extends OpenApiException {

    public OpenApiClientException(String errorMsg) {
        super(errorMsg);
    }

    public OpenApiClientException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
    }
}
