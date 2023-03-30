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

package com.taotao.cloud.auth.biz.demo.core.properties;

import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import cn.herodotus.engine.oauth2.core.enums.Certificate;
import com.google.common.base.MoreObjects;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description: OAuth2 配置属性 仅认证服务会使用到的安全相关配置，这是与 SecurityProperties 的主要区别。
 *
 * @author : gengwei.zheng
 * @date : 2022/3/6 16:36
 */
@ConfigurationProperties(prefix = OAuth2Constants.PROPERTY_PREFIX_OAUTH2)
public class OAuth2Properties {

    private Jwk jwk = new Jwk();

    public Jwk getJwk() {
        return jwk;
    }

    public void setJwk(Jwk jwk) {
        this.jwk = jwk;
    }

    public static class Jwk {

        private enum Strategy {
            STANDARD,
            CUSTOM
        }

        /** 证书策略：standard OAuth2 标准证书模式；custom 自定义证书模式 */
        private Certificate certificate = Certificate.CUSTOM;

        /** jks证书文件路径 */
        private String jksKeyStore = "classpath*:certificate/herodotus-cloud.jks";
        /** jks证书密码 */
        private String jksKeyPassword = "Herodotus-Cloud";
        /** jks证书密钥库密码 */
        private String jksStorePassword = "Herodotus-Cloud";
        /** jks证书别名 */
        private String jksKeyAlias = "herodotus-cloud";

        public Certificate getCertificate() {
            return certificate;
        }

        public void setCertificate(Certificate certificate) {
            this.certificate = certificate;
        }

        public String getJksKeyStore() {
            return jksKeyStore;
        }

        public void setJksKeyStore(String jksKeyStore) {
            this.jksKeyStore = jksKeyStore;
        }

        public String getJksKeyPassword() {
            return jksKeyPassword;
        }

        public void setJksKeyPassword(String jksKeyPassword) {
            this.jksKeyPassword = jksKeyPassword;
        }

        public String getJksStorePassword() {
            return jksStorePassword;
        }

        public void setJksStorePassword(String jksStorePassword) {
            this.jksStorePassword = jksStorePassword;
        }

        public String getJksKeyAlias() {
            return jksKeyAlias;
        }

        public void setJksKeyAlias(String jksKeyAlias) {
            this.jksKeyAlias = jksKeyAlias;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("certificate", certificate)
                    .add("jksKeyStore", jksKeyStore)
                    .add("jksKeyPassword", jksKeyPassword)
                    .add("jksStorePassword", jksStorePassword)
                    .add("jksKeyAlias", jksKeyAlias)
                    .toString();
        }
    }

    /**
     * 用于手动的指定 Request Matcher 安全规则。
     *
     * <p>permitAll 比较常用，因此先只增加该项。后续可根据需要添加
     */
    public static class Matcher {
        /** 静态资源过滤 */
        private List<String> staticResources;
        /** Security "permitAll" 权限列表。 */
        private List<String> permitAll;

        public List<String> getStaticResources() {
            return staticResources;
        }

        public void setStaticResources(List<String> staticResources) {
            this.staticResources = staticResources;
        }

        public List<String> getPermitAll() {
            return permitAll;
        }

        public void setPermitAll(List<String> permitAll) {
            this.permitAll = permitAll;
        }
    }
}
