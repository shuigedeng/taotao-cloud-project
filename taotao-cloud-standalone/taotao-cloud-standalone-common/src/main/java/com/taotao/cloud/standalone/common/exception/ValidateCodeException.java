package com.taotao.cloud.standalone.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Classname ValidateCodeException
 * @Description TODO
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-07-07 23:06
 * @Version 1.0
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 5022575393500654459L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
