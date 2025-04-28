package com.taotao.cloud.ccsr.common.exception;

import java.io.Serial;

public class CcsrClientException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6123954847415409614L;

    public CcsrClientException(final Throwable e) {
        super(e);
    }

    public CcsrClientException(final String message) {
        super(message);
    }

    public CcsrClientException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
