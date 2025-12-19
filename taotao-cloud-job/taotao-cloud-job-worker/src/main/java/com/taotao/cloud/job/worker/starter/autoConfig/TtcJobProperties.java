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

package com.taotao.cloud.job.worker.starter.autoConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;

/**
 * TtcJobProperties
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@ConfigurationProperties(prefix = "ttcjob")
public class TtcJobProperties {

    private final Worker worker = new Worker();

    public Worker getWorker() {
        return worker;
    }

    @Deprecated
    @DeprecatedConfigurationProperty(replacement = "ttcjob.worker.app-name")
    public String getAppName() {
        return getWorker().appName;
    }

    @Deprecated
    public void setAppName( String appName ) {
        getWorker().setAppName(appName);
    }

    @DeprecatedConfigurationProperty(replacement = "ttcjob.worker.grpc-port")
    public int getGrpcPort() {
        return getWorker().getPort();
    }

    public void setGrpcPort( int grpcPort ) {
        getWorker().setPort(grpcPort);
    }

    @Deprecated
    @DeprecatedConfigurationProperty(replacement = "ttcjob.worker.server-address")
    public String getServerAddress() {
        return getWorker().serverAddress;
    }

    @Deprecated
    public void setServerAddress( String serverAddress ) {
        getWorker().setServerAddress(serverAddress);
    }

    @Deprecated
    @DeprecatedConfigurationProperty(replacement = "ttcjob.worker.name-server-address")
    public String getNameServerAddress() {
        return getWorker().nameServerAddress;
    }

    @Deprecated
    public void setNameServerAddress( String address ) {
        getWorker().setNameServerAddress(address);
    }

    @Deprecated
    @DeprecatedConfigurationProperty(replacement = "ttcjob.worker.server-port")
    public int getServerPort() {
        return getWorker().serverPort;
    }

    /**
     * kjob worker configuration properties.
     */
    @Setter
    @Getter
    public static class Worker {

        /**
         * Whether to enable kJob Worker
         */
        private boolean enabled = true;

        /**
         * Name of application, String type. Total length of this property should be no more than 255 characters. This
         * is one of the required properties when registering a new application. This property should be assigned with
         * the same value as what you entered for the appName.
         */
        private String appName;

        /**
         * port
         */
        private Integer port;

        /**
         * only ip address
         */
        private String serverAddress;

        /**
         * only server port
         */
        private Integer serverPort;

        private String nameServerAddress;

        /**
         * Max numbers of LightTaskTacker
         */
        private Integer maxLightweightTaskNum = 1024;

        /**
         * Max numbers of HeavyTaskTacker
         */
        private Integer maxHeavyweightTaskNum = 64;

        /**
         * Interval(s) of worker health report
         */
        private Integer healthReportInterval = 10;
    }
}
