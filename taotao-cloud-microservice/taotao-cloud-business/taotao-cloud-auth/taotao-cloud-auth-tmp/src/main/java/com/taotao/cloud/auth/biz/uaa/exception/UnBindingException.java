package com.taotao.cloud.auth.biz.uaa.exception;


import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;

/**
 * 解绑(第三方)异常
 * @author YongWu zheng
 * @weixin z56133
 * @since 2021.2.24 14:29
 */
public class UnBindingException extends BusinessException {

    public UnBindingException(ErrorCodeEnum errorCodeEnum, Object data) {
        super(errorCodeEnum, data);
    }

    public UnBindingException(ErrorCodeEnum errorCodeEnum, Object data, Throwable cause) {
        super(errorCodeEnum, data, cause);
    }

}
