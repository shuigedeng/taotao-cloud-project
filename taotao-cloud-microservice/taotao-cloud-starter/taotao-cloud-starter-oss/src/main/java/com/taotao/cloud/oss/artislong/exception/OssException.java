package com.taotao.cloud.oss.artislong.exception;

/**
 * @author 陈敏
 * @version OssException.java, v 1.1 2021/12/3 21:02 chenmin Exp $
 * Created on 2021/12/3
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
