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

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * <p>Description: 服务上下文信息工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/14 17:28
 */
public class ServiceContext {

    private static volatile ServiceContext instance;

    /**
     * 平台架构类型，默认：DISTRIBUTED（分布式架构）
     */
    private Architecture architecture = Architecture.DISTRIBUTED;
    /**
     * 数据访问策略，默认：
     */
    private Target dataAccessStrategy = Target.REMOTE;

    /**
     * 协议头类型
     */
    private Protocol protocol = Protocol.HTTP;
    /**
     * 服务端口号
     */
    private String port;
    /**
     * 服务IP地址
     */
    private String ip;
    /**
     * 服务地址，格式：ip:port
     */
    private String address;
    /**
     * 服务Url，格式：http://ip:port
     */
    private String url;
    /**
     * 应用名称，与spring.application.name一致
     */
    private String applicationName;
    /**
     * 认证中心服务名称
     */
    private String uaaServiceName;
    /**
     * 用户中心服务名称
     */
    private String upmsServiceName;
    /**
     * 网关地址
     */
    private String gatewayAddress;
    /**
     * 留存一份ApplicationContext
     */
    private ApplicationContext applicationContext;

    private ServiceContext() {

    }

    public static ServiceContext getInstance() {
        if (ObjectUtils.isEmpty(instance)) {
            synchronized (ServiceContext.class) {
                if (ObjectUtils.isEmpty(instance)) {
                    instance = new ServiceContext();
                }
            }
        }

        return instance;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

    public Target getDataAccessStrategy() {
        return dataAccessStrategy;
    }

    public void setDataAccessStrategy(Target dataAccessStrategy) {
        this.dataAccessStrategy = dataAccessStrategy;
    }

    public boolean isDistributedArchitecture() {
        return this.getArchitecture() == Architecture.DISTRIBUTED;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getGatewayAddress() {
        return gatewayAddress;
    }

    public void setGatewayAddress(String gatewayAddress) {
        this.gatewayAddress = gatewayAddress;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        if (isDistributedArchitecture()) {
            this.address = this.getGatewayAddress() + SymbolConstants.FORWARD_SLASH + this.getApplicationName();
        } else {
            if (StringUtils.isNotBlank(this.ip) && StringUtils.isNotBlank(this.port)) {
                this.address = this.ip + SymbolConstants.COLON + this.port;
            }
        }
        return address;
    }

    public String getUrl() {
        if (StringUtils.isBlank(this.url)) {
            String address = this.getAddress();
            if (StringUtils.isNotBlank(address)) {
                return ConvertUtils.addressToUri(address, getProtocol(), false);
            }
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getUaaServiceName() {
        return uaaServiceName;
    }

    public void setUaaServiceName(String uaaServiceName) {
        this.uaaServiceName = uaaServiceName;
    }

    public String getUpmsServiceName() {
        return upmsServiceName;
    }

    public void setUpmsServiceName(String upmsServiceName) {
        this.upmsServiceName = upmsServiceName;
    }

    public String getOriginService() {
        return getApplicationName() + SymbolConstants.COLON + getPort();
    }

    public void publishEvent(ApplicationEvent applicationEvent) {
        getApplicationContext().publishEvent(applicationEvent);
    }
}
