package com.taotao.cloud.core.heaven.response.respcode.impl;


import com.taotao.cloud.core.heaven.response.respcode.AdviceRespCode;
import com.taotao.cloud.core.heaven.response.respcode.RespCode;

/**
 * 包含建议的响应码
 */
public enum CommonAdviceRespCode implements AdviceRespCode {
    ;

    /**
     * 编码
     * @since 0.1.38
     */
    private final String code;

    /**
     * 消息
     * @since 0.1.38
     */
    private final String msg;

    /**
     * 建议
     * @since 0.1.38
     */
    private final String advice;

    CommonAdviceRespCode(String code, String msg, String advice) {
        this.code = code;
        this.msg = msg;
        this.advice = advice;
    }


    CommonAdviceRespCode(final RespCode respCode, String advice) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
        this.advice = advice;
    }

    @Override
    public String getAdvice() {
        return this.advice;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "CommonAdviceRespCode{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", advice='" + advice + '\'' +
                '}';
    }

}
