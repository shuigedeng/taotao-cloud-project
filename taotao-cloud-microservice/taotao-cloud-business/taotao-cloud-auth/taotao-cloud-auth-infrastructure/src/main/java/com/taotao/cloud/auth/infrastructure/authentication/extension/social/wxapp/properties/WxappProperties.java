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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.properties;

import com.google.common.base.MoreObjects;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.constants.AccessConstants;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.wxapp.enums.MiniProgramState;
import java.io.Serializable;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>微信小程序配置属性 </p>
 *
 *
 * @since : 2021/3/26 17:27
 */
@ConfigurationProperties(prefix = AccessConstants.PROPERTY_ACCESS_WXAPP)
public class WxappProperties implements Serializable {

    /**
     * 是否开启
     */
    private Boolean enabled;
    /**
     * 默认App Id
     */
    private String defaultAppId;

    /**
     * 小程序配置列表
     */
    private List<Config> configs;

    /**
     * 小程序订阅消息配置列表
     */
    private List<Subscribe> subscribes;

    public String getDefaultAppId() {
        return defaultAppId;
    }

    public void setDefaultAppId(String defaultAppId) {
        this.defaultAppId = defaultAppId;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public List<Subscribe> getSubscribes() {
        return subscribes;
    }

    public void setSubscribes(List<Subscribe> subscribes) {
        this.subscribes = subscribes;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appId;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String messageDataFormat;

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

        public String getMessageDataFormat() {
            return messageDataFormat;
        }

        public void setMessageDataFormat(String messageDataFormat) {
            this.messageDataFormat = messageDataFormat;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("appid", appId)
                    .add("secret", secret)
                    .add("token", token)
                    .add("aesKey", aesKey)
                    .add("messageDataFormat", messageDataFormat)
                    .toString();
        }
    }

    public static class Subscribe {

        /**
         * 订阅消息指定的小程序跳转页面地址
         */
        private String redirectPage;
        /**
         * 订阅消息模版ID
         */
        private String templateId;

        /**
         * 自定义Message区分ID，用于获取不同的SubscribeMessageHandler
         */
        private String subscribeId;

        private MiniProgramState miniProgramState = MiniProgramState.formal;

        public String getRedirectPage() {
            return redirectPage;
        }

        public void setRedirectPage(String redirectPage) {
            this.redirectPage = redirectPage;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public MiniProgramState getMiniProgramState() {
            return miniProgramState;
        }

        public void setMiniProgramState(MiniProgramState miniProgramState) {
            this.miniProgramState = miniProgramState;
        }

        public String getSubscribeId() {
            return subscribeId;
        }

        public void setSubscribeId(String subscribeId) {
            this.subscribeId = subscribeId;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("redirectPage", redirectPage)
                    .add("templateId", templateId)
                    .add("subscribeId", subscribeId)
                    .add("miniProgramState", miniProgramState)
                    .toString();
        }
    }
}
