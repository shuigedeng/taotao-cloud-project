package com.taotao.cloud.payment.biz.bootx.core.pay.local;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
* 异步退款线程变量
* @author xxm  
* @date 2022/3/9 
*/
public final class AsyncRefundLocal {
    private static final ThreadLocal<String> THREAD_LOCAL = new TransmittableThreadLocal<>();
    private static final ThreadLocal<String> ERROR_MSG = new TransmittableThreadLocal<>();
    private static final ThreadLocal<String> ERROR_CODE = new TransmittableThreadLocal<>();
    /**
     * 设置
     */
    public static void set(String refundId) {
        THREAD_LOCAL.set(refundId);
    }

    /**
     * 获取
     */
    public static String get() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置
     */
    public static void setErrorMsg(String errorMsg) {
        ERROR_MSG.set(errorMsg);
    }

    /**
     * 获取
     */
    public static String getErrorMsg() {
        return ERROR_MSG.get();
    }
    /**
     * 设置
     */
    public static void setErrorCode(String errorCode) {
        ERROR_CODE.set(errorCode);
    }

    /**
     * 获取
     */
    public static String getErrorCode() {
        return ERROR_CODE.get();
    }

    /**
     * 清除
     */
    public static void clear() {
        THREAD_LOCAL.remove();
        ERROR_MSG.remove();
        ERROR_CODE.remove();
    }

}
