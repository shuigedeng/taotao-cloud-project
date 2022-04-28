package com.taotao.cloud.oss.artislong.exception;

/**
 * oss例外
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:35:06
 */
public class OssException extends RuntimeException {
    public OssException() {
    }

    public OssException(String message) {
        super(message);
    }

    public OssException(String message, Throwable cause) {
        super(message, cause);
    }

    public OssException(Throwable cause) {
        super(cause);
    }

    public OssException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
