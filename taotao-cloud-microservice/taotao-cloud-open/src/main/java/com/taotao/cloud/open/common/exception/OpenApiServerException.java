package com.taotao.cloud.open.common.exception;

/**
 * OpenApi服务端异常
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:09:37
 */
public class OpenApiServerException extends OpenApiException {
    public OpenApiServerException(String errorMsg) {
        super(errorMsg);
    }

    public OpenApiServerException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
    }
}
