package com.taotao.cloud.ccsr.common.exception;

import java.io.Serial;


public class InitializationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6123954847415222614L;

    public InitializationException(final Throwable e) {
        super(e);
    }

    public InitializationException(final String message) {
        super(message);
    }

    public InitializationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
