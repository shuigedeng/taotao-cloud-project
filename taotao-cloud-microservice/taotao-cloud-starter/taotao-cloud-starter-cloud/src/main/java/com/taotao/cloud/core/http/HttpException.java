package com.taotao.cloud.core.http;


import com.taotao.cloud.common.exception.BaseException;

/**
 * @author: chejiangyi
 * @version: 2019-07-22 20:34
 **/
public class HttpException extends BaseException {
    public HttpException(Throwable exp)
    {
        super(exp);
    }

    public HttpException(String message)
    {
        super(message);
    }

    public HttpException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
