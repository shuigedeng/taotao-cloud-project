package com.taotao.cloud.workflow.api.common.base;

/**
 *
 */
public enum ActionResultCode{

    /**
     * 成功
     */
    Success(200, "成功"),
    /**
     * 失败
     */
    Fail(400, "失败"),
    /**
     * 验证错误
     */
    ValidateError(401, "验证错误"),
    /**
     * 异常
     */
    Exception(500, "异常"),
    /**
     * 登录过期
     */
    SessionOverdue(600, "登录过期,请重新登录"),
    /**
     * 异地登录
     */
    SessionOffLine(601, "您的帐号在其他地方已登录,被强制踢出"),
    /**
     * token验证失败
     */
    SessionError(602, "Token验证失败");

    private int code;
    private String message;

    ActionResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
