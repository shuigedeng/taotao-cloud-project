/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
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

package com.taotao.cloud.auth.biz.demo.core.properties;

import cn.herodotus.engine.assistant.core.enums.Target;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * <p> Description : 服务安全配置 </p>
 * <p>
 * 所有服务会使用到的安全相关配置，包括认证服务和资源服务。
 *
 * @author : gengwei.zheng
 * @date : 2019/11/28 13:08
 */
@ConfigurationProperties(prefix = OAuth2Constants.PROPERTY_PREFIX_SECURITY)
public class SecurityProperties implements Serializable {

    /**
     * Token 校验是采用远程方式还是本地方式。
     */
    private Target validate = Target.REMOTE;
    private Matcher matcher = new Matcher();

    public Target getValidate() {
        return validate;
    }

    public void setValidate(Target validate) {
        this.validate = validate;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    /**
     * 用于手动的指定 Request Matcher 安全规则。
     * <p>
     * permitAll 比较常用，因此先只增加该项。后续可根据需要添加
     */
    public static class Matcher {
        /**
         * 静态资源过滤
         */
        private List<String> staticResources;
        /**
         * Security "permitAll" 权限列表。
         */
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
