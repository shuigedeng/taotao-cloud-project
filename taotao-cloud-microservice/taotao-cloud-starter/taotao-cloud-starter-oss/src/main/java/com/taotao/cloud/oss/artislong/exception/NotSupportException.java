package com.taotao.cloud.oss.artislong.exception;

/**
 * @author 陈敏
 * @version NotSupportException.java, v 1.1 2021/12/3 15:04 chenmin Exp $
 * Created on 2021/12/3
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
