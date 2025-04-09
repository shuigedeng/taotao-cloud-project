package com.taotao.cloud.ccsr.common.exception;

import java.io.Serial;

public class OHaraMcsClientException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6123954847415409614L;

    public OHaraMcsClientException(final Throwable e) {
        super(e);
    }

    public OHaraMcsClientException(final String message) {
        super(message);
    }

    public OHaraMcsClientException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
