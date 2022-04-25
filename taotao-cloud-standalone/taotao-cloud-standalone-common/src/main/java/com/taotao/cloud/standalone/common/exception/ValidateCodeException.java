package com.taotao.cloud.standalone.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Classname ValidateCodeException
 * @Description TODO
 * @Author shuigedeng
 * @since 2019-07-07 23:06
 *
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 5022575393500654459L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
