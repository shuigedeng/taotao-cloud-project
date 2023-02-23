package com.taotao.cloud.payment.biz.jeepay.jeepay.exception;

/**
 * Jeepay异常抽象类
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-08 11:00
 */
public abstract class JeepayException extends Exception {

    private static final long serialVersionUID = 2566087783987900120L;

    private int statusCode;

    public JeepayException(String message) {
        super(message, null);
    }

    public JeepayException(String message, Throwable e) {
        super(message, e);
    }

    public JeepayException(String message, int statusCode, Throwable e) {
        super(message, e);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
                return sb.toString();
    }
}
