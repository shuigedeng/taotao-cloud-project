package com.taotao.cloud.core.heaven.response.exception;


import com.taotao.cloud.core.heaven.response.respcode.AdviceRespCode;
import com.taotao.cloud.core.heaven.response.respcode.RespCode;

/**
 * 通用运行时异常
 * 1. 基于 {@link AdviceRespCode} 响应码
 *
 */
public class GenericAdviceRuntimeException extends GenericRuntimeException implements
	AdviceRespCode {

    /**
     * 响应建议
     *
     * @since 0.1.38
     */
    private final String advice;

    public GenericAdviceRuntimeException(RespCode respCode, String advice) {
        super(respCode);
        this.advice = advice;
    }

    public GenericAdviceRuntimeException(String message, RespCode respCode, String advice) {
        super(message, respCode);
        this.advice = advice;
    }

    public GenericAdviceRuntimeException(String message, Throwable cause, RespCode respCode, String advice) {
        super(message, cause, respCode);
        this.advice = advice;
    }

    public GenericAdviceRuntimeException(Throwable cause, RespCode respCode, String advice) {
        super(cause, respCode);
        this.advice = advice;
    }

    public GenericAdviceRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RespCode respCode, String advice) {
        super(message, cause, enableSuppression, writableStackTrace, respCode);
        this.advice = advice;
    }

    public GenericAdviceRuntimeException(AdviceRespCode adviceRespCode) {
        super(adviceRespCode);
        this.advice = adviceRespCode.getAdvice();
    }

    public GenericAdviceRuntimeException(String message, AdviceRespCode adviceRespCode) {
        super(message, adviceRespCode);
        this.advice = adviceRespCode.getAdvice();
    }

    public GenericAdviceRuntimeException(String message, Throwable cause, AdviceRespCode adviceRespCode) {
        super(message, cause, adviceRespCode);
        this.advice = adviceRespCode.getAdvice();
    }

    public GenericAdviceRuntimeException(Throwable cause, AdviceRespCode adviceRespCode) {
        super(cause, adviceRespCode);
        this.advice = adviceRespCode.getAdvice();
    }

    public GenericAdviceRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AdviceRespCode adviceRespCode) {
        super(message, cause, enableSuppression, writableStackTrace, adviceRespCode);
        this.advice = adviceRespCode.getAdvice();
    }

    @Override
    public String getAdvice() {
        return advice;
    }

}
