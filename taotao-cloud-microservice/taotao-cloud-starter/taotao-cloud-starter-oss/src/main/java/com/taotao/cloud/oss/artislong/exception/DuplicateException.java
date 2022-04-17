package com.taotao.cloud.oss.artislong.exception;

/**
 * @author 陈敏
 * @version DuplicateException.java, v 1.1 2021/11/15 15:27 chenmin Exp $
 * Created on 2021/11/15
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
