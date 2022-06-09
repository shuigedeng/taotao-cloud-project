package com.taotao.cloud.oss.common.exception;

/**
 * 重复例外
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:35:04
 */
public class DuplicateException extends OssException {
    public DuplicateException() {
        super();
    }

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateException(Throwable cause) {
        super(cause);
    }

    protected DuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
