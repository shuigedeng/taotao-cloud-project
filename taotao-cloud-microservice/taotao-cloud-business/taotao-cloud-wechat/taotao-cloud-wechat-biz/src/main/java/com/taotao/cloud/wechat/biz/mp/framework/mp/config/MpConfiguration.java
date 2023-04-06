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

package com.taotao.cloud.wechat.biz.mp.framework.mp.config;

import cn.iocoder.yudao.module.mp.framework.mp.core.DefaultMpServiceFactory;
import cn.iocoder.yudao.module.mp.framework.mp.core.MpServiceFactory;
import cn.iocoder.yudao.module.mp.service.handler.menu.MenuHandler;
import cn.iocoder.yudao.module.mp.service.handler.message.MessageAutoReplyHandler;
import cn.iocoder.yudao.module.mp.service.handler.message.MessageReceiveHandler;
import cn.iocoder.yudao.module.mp.service.handler.other.KfSessionHandler;
import cn.iocoder.yudao.module.mp.service.handler.other.NullHandler;
import cn.iocoder.yudao.module.mp.service.handler.other.ScanHandler;
import cn.iocoder.yudao.module.mp.service.handler.other.StoreCheckNotifyHandler;
import cn.iocoder.yudao.module.mp.service.handler.user.LocationHandler;
import cn.iocoder.yudao.module.mp.service.handler.user.SubscribeHandler;
import cn.iocoder.yudao.module.mp.service.handler.user.UnsubscribeHandler;
import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 微信公众号的配置类
 *
 * @author 芋道源码
 */
@Configuration
public class MpConfiguration {

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RedisTemplateWxRedisOps redisTemplateWxRedisOps(StringRedisTemplate stringRedisTemplate) {
        return new RedisTemplateWxRedisOps(stringRedisTemplate);
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MpServiceFactory mpServiceFactory(
            RedisTemplateWxRedisOps redisTemplateWxRedisOps,
            WxMpProperties wxMpProperties,
            MessageReceiveHandler messageReceiveHandler,
            KfSessionHandler kfSessionHandler,
            StoreCheckNotifyHandler storeCheckNotifyHandler,
            MenuHandler menuHandler,
            NullHandler nullHandler,
            SubscribeHandler subscribeHandler,
            UnsubscribeHandler unsubscribeHandler,
            LocationHandler locationHandler,
            ScanHandler scanHandler,
            MessageAutoReplyHandler messageAutoReplyHandler) {
        return new DefaultMpServiceFactory(
                redisTemplateWxRedisOps,
                wxMpProperties,
                messageReceiveHandler,
                kfSessionHandler,
                storeCheckNotifyHandler,
                menuHandler,
                nullHandler,
                subscribeHandler,
                unsubscribeHandler,
                locationHandler,
                scanHandler,
                messageAutoReplyHandler);
    }
}
