package com.taotao.cloud.ccsr.common.exception;

import java.io.Serial;


public class DestroyException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6123954866415409614L;

    public DestroyException(final Throwable e) {
        super(e);
    }

    public DestroyException(final String message) {
        super(message);
    }

    public DestroyException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
