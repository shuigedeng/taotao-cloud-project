package com.taotao.cloud.payment.biz.jeepay.jeepay.exception;

/**
 * API连接异常
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-08 11:31
 */
public class APIConnectionException extends JeepayException {

    private static final long serialVersionUID = -8764189839522042543L;

    public APIConnectionException(String message) {
        super(message);
    }

    public APIConnectionException(String message, Throwable e) {
        super(message, e);
    }

}
