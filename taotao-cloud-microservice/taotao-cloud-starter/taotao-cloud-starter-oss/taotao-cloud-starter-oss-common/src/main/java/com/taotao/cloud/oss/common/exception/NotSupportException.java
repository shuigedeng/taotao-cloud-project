package com.taotao.cloud.oss.common.exception;

/**
 * 不支持例外
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:35:08
 */
public class NotSupportException extends OssException {
    public NotSupportException() {
        super();
    }

    public NotSupportException(String message) {
        super(message);
    }

    public NotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportException(Throwable cause) {
        super(cause);
    }

    protected NotSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
