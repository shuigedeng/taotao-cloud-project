/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.exception;

import java.util.Locale;

/**
 * 短信发送客户端错误
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:47:33
 */
public class SendClientException extends SmsException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "短信发送失败，客户端错误，";
        } else {
            DEFAULT_MSG = "SMS sending failed with client exception, ";
        }
    }

    /**
     * 通过错误信息构造短信发送客户端错误
     *
     * @param message
     *         错误信息
     */
    public SendClientException(String message) {
        super(DEFAULT_MSG + message);
    }

    /**
     * 通过错误信息构造短信发送客户端错误
     *
     * @param message
     *         错误信息
     * @param cause
     *         源异常
     */
    public SendClientException(String message, Throwable cause) {
        super(DEFAULT_MSG + message, cause);
    }
}
