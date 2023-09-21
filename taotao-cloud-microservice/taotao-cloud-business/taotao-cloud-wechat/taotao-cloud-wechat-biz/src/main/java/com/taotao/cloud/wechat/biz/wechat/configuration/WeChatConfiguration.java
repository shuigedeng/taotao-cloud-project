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

package com.taotao.cloud.wechat.biz.wechat.configuration;

import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置
 *
 * @author xxm
 * @since 2022/7/15
 */
@Configuration
@RequiredArgsConstructor
public class WeChatConfiguration {

    private final WeChatProperties weChatProperties;

    /** 微信公众号APIService */
    @Bean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    /** 微信配置 */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(weChatProperties.getAppId()); // 设置微信公众号的appid
        config.setSecret(weChatProperties.getAppSecret()); // 设置微信公众号的app corpSecret
        config.setToken(weChatProperties.getToken()); // 设置微信公众号的Token
        config.setAesKey(weChatProperties.getEncodingAesKey()); // 消息加解密密钥
        return config;
    }
}
