/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.response.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常工具类
 */
public final class ExceptionUtil {

    private ExceptionUtil(){}

    /**
     * 解包异常
     * @param wrapped 异常
     * @return 被解包的异常
     */
    public static Throwable unwrapThrowable(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }


    /**
     * 抛出运行时不支持的操作异常
     */
    public static void throwUnsupportedOperationException() {
        throw new UnsupportedOperationException();
    }

    /**
     * 将CheckedException转换为UncheckedException.
     * @param ex    ex 异常
     * @return 运行时异常
     */
    public static RuntimeException unchecked(Throwable ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        } else {
            return new RuntimeException(ex);
        }
    }

    /**
     * 将ErrorStack转化为String.
     * @param ex 异常
     * @return 返回异常内容
     */
    public static String getStackTraceAsString(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 获取组合本异常信息与底层异常信息的异常描述, 适用于本异常为统一包装异常类，底层异常才是根本原因的情况。
     * @param ex    异常
     * @return  异常信息
     */
    public static String getErrorMessageWithNestedException(Throwable ex) {
        Throwable nestedException = ex.getCause();
        return ex.getMessage() + " nested exception is " +
                nestedException.getClass().getName() + ":" + nestedException.getMessage();
    }


    /**
     * 获取异常的Root Cause.
     * @param ex 异常
     * @return  异常的 RootCause.
     */
    public static Throwable getRootCause(Throwable ex) {
        Throwable cause;
        Throwable result = null;
        while ((cause = ex.getCause()) != null) {
            result = cause;
        }
        return result;
    }


    /**
     * 判断异常是否由某些底层的异常引起.
     * @param ex    异常
     * @param causeExceptionClasses 导致的异常原因
     * @return  {@code true} 是否由某个异常引起
     */
    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 获取确切的异常信息
     * 1. 主要针对代理报错
     * @param throwable 异常
     * @return 确切的异常信息
     * @since 0.1.4
     */
    public static Throwable getActualThrowable(final Throwable throwable) {
        if(InvocationTargetException.class.equals(throwable.getClass())) {
            InvocationTargetException exception = (InvocationTargetException) throwable;
            return exception.getTargetException();
        }
        return throwable;
    }

}
