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
 * 重试时间过短
 *
 * @author shuigedeng
 */
public class RetryTimeShortException extends SmsException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG;

    /**
     * 剩余时间,单位秒
     */
    private final long surplus;

    static {
        Locale locale = Locale.getDefault();

        if (Locale.CHINA.equals(locale)) {
            DEFAULT_MSG = "重试时间过短，请等待%d秒后重试";
        } else {
            DEFAULT_MSG = "Retry time is short, please wait %d second and try again.";
        }
    }

    /**
     * 重试时间过短
     *
     * @param surplus 剩余时间,单位秒
     */
    public RetryTimeShortException(long surplus) {
        super(String.format(DEFAULT_MSG, surplus));
        this.surplus = surplus;
    }

    /**
     * 获取剩余时间,单位秒
     *
     * @return 剩余时间
     */
    public long getSurplus() {
        return surplus;
    }
}
