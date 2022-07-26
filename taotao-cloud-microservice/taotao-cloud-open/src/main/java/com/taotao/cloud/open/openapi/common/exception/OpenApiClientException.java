package com.taotao.cloud.open.openapi.common.exception;

/**
 * OpenApi客户端异常
 *
 * @author wanghuidong
 * 时间： 2022/6/17 21:12
 */
public class OpenApiClientException extends OpenApiException {

    public OpenApiClientException(String errorMsg) {
        super(errorMsg);
    }

    public OpenApiClientException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
    }
}
