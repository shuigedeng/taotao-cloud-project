package com.taotao.cloud.im.biz.platform.common.exception;

import com.platform.common.enums.ResultCodeEnum;
import lombok.Getter;
import org.apache.shiro.authc.AuthenticationException;

/**
 * 登录异常
 */
public class LoginException extends AuthenticationException {

    @Getter
    private ResultCodeEnum code;

    public LoginException(String message) {
        super(message);
        this.code = ResultCodeEnum.FAIL;
    }

    public LoginException(ResultCodeEnum resultCode) {
        super(resultCode.getInfo());
        this.code = resultCode;
    }

}
