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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxmpp.configuration;

import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxmpp.processor.WxmppLogHandler;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxmpp.processor.WxmppProcessor;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxmpp.properties.WxmppProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>微信公众号配置 </p>
 *
 *
 * @since : 2021/4/7 13:25
 */
@Configuration(proxyBeanMethods = false)
// @ConditionalOnWxmppEnabled
@EnableConfigurationProperties(WxmppProperties.class)
public class WxmppConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WxmppConfiguration.class);

    @PostConstruct
    public void init() {
        log.debug("SDK [Access Wxmpp] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public WxmppProcessor wxmppProcessor(WxmppProperties wxmppProperties, StringRedisTemplate stringRedisTemplate) {
        WxmppProcessor wxmppProcessor = new WxmppProcessor();
        wxmppProcessor.setWxmppProperties(wxmppProperties);
        wxmppProcessor.setWxmppLogHandler(new WxmppLogHandler());
        wxmppProcessor.setStringRedisTemplate(stringRedisTemplate);
        log.trace("Bean [Wxmpp Processor] Auto Configure.");
        return wxmppProcessor;
    }
}
