package com.taotao.cloud.core.heaven.response.exception;


import com.taotao.cloud.core.heaven.response.respcode.RespCode;
import com.taotao.cloud.core.heaven.util.lang.StringUtil;

/**
 * 通用运行时异常
 * 1. 基于 {@link com.github.houbb.heaven.response.respcode.RespCode} 响应码
 * @see CommonRuntimeException 通用运行时异常
 */
public class GenericRuntimeException extends RuntimeException implements RespCode {

    /**
     * 响应码
     */
    private final RespCode respCode;

    public GenericRuntimeException(RespCode respCode) {
        this.respCode = respCode;
    }

    public GenericRuntimeException(String message, RespCode respCode) {
        super(message);
        this.respCode = respCode;
    }

    public GenericRuntimeException(String message, Throwable cause, RespCode respCode) {
        super(message, cause);
        this.respCode = respCode;
    }

    public GenericRuntimeException(Throwable cause, RespCode respCode) {
        super(cause);
        this.respCode = respCode;
    }

    public GenericRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RespCode respCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.respCode = respCode;
    }

    @Override
    public String getCode() {
        return respCode.getCode();
    }

    @Override
    public String getMsg() {
        return respCode.getMsg();
    }

    /**
     * 将枚举的信息+错误的信息
     * @return 混合的信息
     */
    public String getMsgMixed() {
        if(StringUtil.isNotEmpty(super.getMessage())) {
            return respCode.getMsg()+","+super.getMessage();
        }
        return this.getMsg();
    }

    /**
     * 如果指定了 Message,则直接返回 message
     * 否则返回枚举本身的信息
     * @return 信息
     */
    public String getMsgPerfer() {
        if(StringUtil.isNotEmpty(super.getMessage())) {
            return super.getMessage();
        }
        return this.getMsg();
    }
}
