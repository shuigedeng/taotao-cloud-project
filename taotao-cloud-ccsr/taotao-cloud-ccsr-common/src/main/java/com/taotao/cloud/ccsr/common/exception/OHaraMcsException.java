package com.taotao.cloud.ccsr.common.exception;

import java.io.Serial;

/**
 * @author ohara
 * @date 2025/3/14 20:53
 */
public final class OHaraMcsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6123954847415409614L;

    public OHaraMcsException(final Throwable e) {
        super(e);
    }

    public OHaraMcsException(final String message) {
        super(message);
    }

    public OHaraMcsException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
