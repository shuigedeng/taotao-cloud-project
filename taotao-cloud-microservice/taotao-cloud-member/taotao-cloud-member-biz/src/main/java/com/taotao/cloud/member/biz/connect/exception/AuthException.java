package com.taotao.cloud.member.biz.connect.exception;


import com.taotao.cloud.member.biz.connect.config.ConnectAuth;
import com.taotao.cloud.member.biz.connect.entity.enums.AuthResponseStatus;

/**
 * JustAuth通用异常类
 */
public class AuthException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public AuthException(String errorMsg) {
        this(AuthResponseStatus.FAILURE.getCode(), errorMsg);
    }

    public AuthException(String errorMsg, ConnectAuth source) {
        this(AuthResponseStatus.FAILURE.getCode(), errorMsg, source);
    }

    public AuthException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public AuthException(AuthResponseStatus status) {
        this(status.getCode(), status.getMsg());
    }

    public AuthException(int errorCode, String errorMsg, ConnectAuth source) {
        this(errorCode, String.format("%s [%s]", errorMsg, source.getName()));
    }

    public AuthException(AuthResponseStatus status, ConnectAuth source) {
        this(status.getCode(), status.getMsg(), source);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
