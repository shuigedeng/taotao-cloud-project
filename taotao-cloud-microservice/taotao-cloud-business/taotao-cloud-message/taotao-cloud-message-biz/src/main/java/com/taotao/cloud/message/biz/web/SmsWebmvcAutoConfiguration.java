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

package com.taotao.cloud.message.biz.web;

import com.taotao.cloud.sms.common.configuration.SmsAutoConfiguration;
import com.taotao.cloud.sms.common.service.NoticeService;
import com.taotao.cloud.sms.common.service.VerificationCodeService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 短信webmvc自动配置
 *
 * @author shuigedeng
 */
@AutoConfiguration(after = SmsAutoConfiguration.class)
@EnableConfigurationProperties(SmsWebmvcProperties.class)
@ConditionalOnProperty(prefix = SmsWebmvcProperties.PREFIX, name = "enable", havingValue = "true")
public class SmsWebmvcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SmsController.class)
    public SmsController smsController(VerificationCodeService verificationCodeService, NoticeService noticeService) {
        return new SmsController(verificationCodeService, noticeService);
    }
}
