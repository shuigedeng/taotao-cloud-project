package com.taotao.cloud.ccsr.common.exception;

import java.io.Serial;

/**
 * @author ohara
 * @date 2025/3/14 22:44
 */
public class DeserializationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6123954847415409614L;

    private static final String MSG_FOR_SPECIFIED_CLASS = "OHaraMcs deserialize for class [%s] failed. ";

    private static final String ERROR_MSG_FOR_SPECIFIED_CLASS = "OHaraMcs deserialize for class [%s] failed, cause error[%s]. ";


    private Class<?> targetClass;

    public DeserializationException(final Throwable e) {
        super(e);
    }

    public DeserializationException(final String message) {
        super(message);
    }

    public DeserializationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public DeserializationException(Class<?> targetClass, Throwable throwable) {
        super(String.format(ERROR_MSG_FOR_SPECIFIED_CLASS, targetClass.getName(), throwable.getMessage()), throwable);
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }


}
