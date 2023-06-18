/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.wxmpp.properties;

import com.google.common.base.MoreObjects;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.core.constants.AccessConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p>Description: 微信公众号属性配置 </p>
 *
 * 
 * @date : 2021/4/7 13:17
 */
@ConfigurationProperties(prefix = AccessConstants.PROPERTY_ACCESS_WXMPP)
public class WxmppProperties {

    /**
     * 是否开启
     */
    private Boolean enabled;
    /**
     * 是否使用redis存储access token
     */
    private boolean useRedis;

    /**
     * redis 配置
     */
    private RedisConfig redis;

    public boolean isUseRedis() {
        return useRedis;
    }

    public void setUseRedis(boolean useRedis) {
        this.useRedis = useRedis;
    }

    public RedisConfig getRedis() {
        return redis;
    }

    public void setRedis(RedisConfig redis) {
        this.redis = redis;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public static class RedisConfig {
        /**
         * redis服务器 主机地址
         */
        private String host;

        /**
         * redis服务器 端口号
         */
        private Integer port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("host", host)
                    .add("port", port)
                    .toString();
        }
    }

    /**
     * 多个公众号配置信息
     */
    private List<MpConfig> configs;

    public List<MpConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<MpConfig> configs) {
        this.configs = configs;
    }

    public static class MpConfig {
        /**
         * 设置微信公众号的appid
         */
        private String appId;

        /**
         * 设置微信公众号的app secret
         */
        private String secret;

        /**
         * 设置微信公众号的token
         */
        private String token;

        /**
         * 设置微信公众号的EncodingAESKey
         */
        private String aesKey;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAesKey() {
            return aesKey;
        }

        public void setAesKey(String aesKey) {
            this.aesKey = aesKey;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("appId", appId)
                    .add("secret", secret)
                    .add("token", token)
                    .add("aesKey", aesKey)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("useRedis", useRedis)
                .add("redis", redis)
                .add("configs", configs)
                .toString();
    }
}
