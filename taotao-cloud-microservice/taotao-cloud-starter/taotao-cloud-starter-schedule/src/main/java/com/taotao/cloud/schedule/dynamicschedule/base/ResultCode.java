package com.taotao.cloud.schedule.dynamicschedule.base;

/**
 *  返回信息码
 */
public enum ResultCode {
    SUCCESS(0, "操作成功"),
    FAIL(1, "操作失败,请联系管理员"),
    PARAM_ERROR(2, "参数值格式有误"),
    RESOURCE_INVALID(3, "资源无效"),
    TOKEN_INVALID(11, "授权码无效"),
    CUSTOM_ERRORDESC(99);
    ResultCode(int code, String errorDesc) {
        Code = code;
        ErrorDesc = errorDesc;
    }
    ResultCode(int code) {
        Code = code;
    }
    private int Code;
    private String ErrorDesc;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getErrorDesc() {
        return ErrorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        ErrorDesc = errorDesc;
    }
}
