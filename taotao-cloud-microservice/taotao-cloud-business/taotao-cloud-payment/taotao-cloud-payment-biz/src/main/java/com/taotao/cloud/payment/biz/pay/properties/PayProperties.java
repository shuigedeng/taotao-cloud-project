/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.taotao.cloud.payment.biz.pay.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @program: XHuiCloud
 * @description: PayProperties
 * @author: Sinda
 * @create: 2020-06-15 09:37
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "pay")
public class PayProperties {

    private String domain;

    private Alipay alipay;

    private WeChat weChat;

    @Data
    public class Alipay{

        /**
         * 同步通知
         */
        private String returnUrl;

        /**
         * 异步通知
         */
        private String notifyUrl;

        /**
         * 订单过期时间
         */
        private String expireTime;

    }

    @Data
    public class WeChat{

        /**
         * 同步通知
         */
        private String refundNotifyUrl;

        /**
         * 异步通知
         */
        private String notifyUrl;

        /**
         * 订单过期时间
         */
        private String expireTime;

    }
}
