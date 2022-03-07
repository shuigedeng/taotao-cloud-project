package com.taotao.cloud.core.heaven.response.respcode.impl;


import com.taotao.cloud.core.heaven.response.respcode.RespCode;

/**
 * http 响应编码
 */
public enum HttpRespCodeEnum implements RespCode {
    /**
     * 成功
     */
    OK(200, "OK");

    /**
     * 编码
     */
    private final int code;

    /**
     * 信息
     */
    private final String msg;

    HttpRespCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    @Override
    public String getCode() {
        return String.valueOf(this.code);
    }

    /**
     * 整形编码
     * @return 整形
     */
    public int getCodeInt() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
