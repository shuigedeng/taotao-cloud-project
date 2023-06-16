package com.taotao.cloud.auth.biz.uaa.exception;


import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;

/**
 * 绑定(第三方)异常
 * @author YongWu zheng
 * @weixin z56133
 * @since 2021.2.24 14:29
 */
public class BindingException extends BusinessException {

    public BindingException(ErrorCodeEnum errorCodeEnum, Object data) {
        super(errorCodeEnum, data);
    }

    public BindingException(ErrorCodeEnum errorCodeEnum, Object data, Throwable cause) {
        super(errorCodeEnum, data, cause);
    }

}
