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
 * 手机号无效
 *
 * @author shuigedeng
 */
public class PhoneIsNullException extends SmsException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "手机号无效";
        } else {
            DEFAULT_MSG = "Invalid phone number.";
        }
    }

    /**
     * 手机号无效
     */
    public PhoneIsNullException() {
        super(DEFAULT_MSG);
    }
}
