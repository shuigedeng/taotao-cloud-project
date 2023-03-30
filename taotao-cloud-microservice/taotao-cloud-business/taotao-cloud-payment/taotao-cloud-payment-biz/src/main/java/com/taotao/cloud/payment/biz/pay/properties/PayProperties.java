/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    public class Alipay {

        /** 同步通知 */
        private String returnUrl;

        /** 异步通知 */
        private String notifyUrl;

        /** 订单过期时间 */
        private String expireTime;
    }

    @Data
    public class WeChat {

        /** 同步通知 */
        private String refundNotifyUrl;

        /** 异步通知 */
        private String notifyUrl;

        /** 订单过期时间 */
        private String expireTime;
    }
}
