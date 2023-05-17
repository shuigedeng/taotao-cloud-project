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

package com.taotao.cloud.auth.biz.dante.uaa.other;

import com.taotao.cloud.core.properties.EndpointProperties;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;

/**
 * <p>Description: 常用环境信息包装类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/14 16:42
 */
public class HerodotusApplicationContext {

    private final PlatformProperties platformProperties;
    private final EndpointProperties endpointProperties;
    private final ServerProperties serverProperties;
    private final ServiceContext serviceContext;
    private final ApplicationContext applicationContext;

    public HerodotusApplicationContext(ApplicationContext applicationContext, PlatformProperties platformProperties, EndpointProperties endpointProperties, ServerProperties serverProperties) {
        this.platformProperties = platformProperties;
        this.endpointProperties = endpointProperties;
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.serviceContext = ServiceContext.getInstance();
        initServiceContext();
    }

    public void initServiceContext() {
        this.serviceContext.setArchitecture(this.platformProperties.getArchitecture());
        this.serviceContext.setDataAccessStrategy(this.platformProperties.getDataAccessStrategy());
        this.serviceContext.setGatewayAddress(this.endpointProperties.getGatewayServiceUri());
        this.serviceContext.setPort(String.valueOf(this.getPort()));
        this.serviceContext.setIp(getHostAddress());
        this.serviceContext.setApplicationContext(applicationContext);
        this.serviceContext.setApplicationName(PropertyFinder.getApplicationName(applicationContext.getEnvironment()));
    }

    private String getHostAddress() {
        String address = EnvUtils.getHostAddress();
        if (ObjectUtils.isNotEmpty(serverProperties.getAddress())) {
            address = serverProperties.getAddress().getHostAddress();
        }

        if (StringUtils.isNotBlank(address)) {
            return address;
        } else {
            return "localhost";
        }
    }

    private Integer getPort() {
        Integer port = serverProperties.getPort();
        if (ObjectUtils.isNotEmpty(port)) {
            return port;
        } else {
            return 8080;
        }
    }

    public PlatformProperties getPlatformProperties() {
        return platformProperties;
    }

    public ServiceContext getServiceContext() {
        return serviceContext;
    }
}
