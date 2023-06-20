package com.taotao.cloud.auth.biz.uaa.exception;


import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;

/**
 * 解绑(第三方)异常
 */
public class UnBindingException extends BusinessException {

    public UnBindingException(ErrorCodeEnum errorCodeEnum, Object data) {
        super(errorCodeEnum, data);
    }

    public UnBindingException(ErrorCodeEnum errorCodeEnum, Object data, Throwable cause) {
        super(errorCodeEnum, data, cause);
    }

}
