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

package com.taotao.cloud.wechat.biz.module.mp.config;

import com.taotao.cloud.wechat.biz.module.mp.utils.JsonUtils;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Data
@ConfigurationProperties(prefix = "wx.mp")
public class WxMpProperties {
    /** 是否使用redis存储access token */
    private boolean useRedis;

    /** redis 配置 */
    private RedisConfig redisConfig;

    @Data
    public static class RedisConfig {
        /** redis服务器 主机地址 */
        private String host;

        /** redis服务器 端口号 */
        private Integer port;

        /** redis服务器 密码 */
        private String password;

        /** redis 服务连接超时时间 */
        private Integer timeout;
    }

    /** 多个公众号配置信息 */
    private List<MpConfig> configs;

    @Data
    public static class MpConfig {
        /** 设置微信公众号的appid */
        private String appId;

        /** 设置微信公众号的app secret */
        private String secret;

        /** 设置微信公众号的token */
        private String token;

        /** 设置微信公众号的EncodingAESKey */
        private String aesKey;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
