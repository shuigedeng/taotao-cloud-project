package com.taotao.cloud.ccsr.common.exception;

import java.io.Serial;

/**
 * @date 2025/3/14 20:53
 */
public final class CcsrException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6123954847415409614L;

    public CcsrException(final Throwable e) {
        super(e);
    }

    public CcsrException(final String message) {
        super(message);
    }

    public CcsrException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
