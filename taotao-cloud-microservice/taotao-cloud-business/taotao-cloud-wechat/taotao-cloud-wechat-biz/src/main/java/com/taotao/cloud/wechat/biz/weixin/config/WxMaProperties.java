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

package com.taotao.cloud.wechat.biz.weixin.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
@ConfigurationProperties(prefix = "wx.ma")
public class WxMaProperties {

    private List<Config> configs;

    @Data
    public static class Config {
        /** 设置微信小程序的appid */
        private String appId;

        /** 设置微信小程序的Secret */
        private String secret;

        /** 设置微信小程序消息服务器配置的token */
        private String token;

        /** 设置微信小程序消息服务器配置的EncodingAESKey */
        private String aesKey;

        /** 消息格式，XML或者JSON */
        private String msgDataFormat;
        /** 微信支付商户号 */
        private String mchId;
        /** 微信支付商户密钥 */
        private String mchKey;
        /** p12证书的位置，可以指定绝对路径，也可以指定类路径（以classpath:开头） */
        private String keyPath;
    }
}
