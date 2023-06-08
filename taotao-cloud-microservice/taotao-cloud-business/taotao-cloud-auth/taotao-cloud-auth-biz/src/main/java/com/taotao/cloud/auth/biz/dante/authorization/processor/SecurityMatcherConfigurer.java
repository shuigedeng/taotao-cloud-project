/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.authorization.processor;

import com.taotao.cloud.auth.biz.dante.authorization.properties.OAuth2AuthorizationProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 安全过滤配置处理器 </p>
 * <p>
 * 对静态资源、开放接口等静态配置进行处理。整合默认配置和配置文件中的配置
 *
 * @author : gengwei.zheng
 * @date : 2022/3/8 22:57
 */
@Component
public class SecurityMatcherConfigurer {

    private List<String> staticResources;
    private List<String> permitAllResources;
    private List<String> hasAuthenticatedResources;

    private final OAuth2AuthorizationProperties authorizationProperties;

    public SecurityMatcherConfigurer(OAuth2AuthorizationProperties authorizationProperties) {
        this.authorizationProperties = authorizationProperties;
        this.staticResources = new ArrayList<>();
        this.permitAllResources = new ArrayList<>();
        this.hasAuthenticatedResources = new ArrayList<>();
    }


    public List<String> getStaticResourceList() {
        if (CollectionUtils.isEmpty(this.staticResources)) {
            this.staticResources = ListUtils.merge(authorizationProperties.getMatcher().getStaticResources(), WebResources.DEFAULT_IGNORED_STATIC_RESOURCES);
        }
        return this.staticResources;
    }

    public List<String> getPermitAllList() {
        if (CollectionUtils.isEmpty(this.permitAllResources)) {
            this.permitAllResources = ListUtils.merge(authorizationProperties.getMatcher().getPermitAll(), WebResources.DEFAULT_PERMIT_ALL_RESOURCES);
        }
        return this.permitAllResources;
    }

    public List<String> getHasAuthenticatedList() {
        if (CollectionUtils.isEmpty(this.hasAuthenticatedResources)) {
            this.hasAuthenticatedResources = ListUtils.merge(authorizationProperties.getMatcher().getHasAuthenticated(), WebResources.DEFAULT_HAS_AUTHENTICATED_RESOURCES);
        }
        return this.hasAuthenticatedResources;
    }

    public String[] getStaticResourceArray() {
        return ListUtils.toStringArray(getStaticResourceList());
    }

    public String[] getPermitAllArray() {
        return ListUtils.toStringArray(getPermitAllList());
    }
}
