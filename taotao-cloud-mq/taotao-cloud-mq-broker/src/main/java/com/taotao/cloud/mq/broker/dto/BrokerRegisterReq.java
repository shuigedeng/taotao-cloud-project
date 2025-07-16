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

package com.taotao.cloud.mq.broker.dto;

import com.taotao.cloud.mq.common.dto.req.MqCommonReq;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class BrokerRegisterReq extends MqCommonReq {

    /**
     * 服务信息
     */
    private ServiceEntry serviceEntry;

    /**
     * 账户标识
     *
     * @since 2024.05
     */
    private String appKey;

    /**
     * 账户密码
     *
     * @since 2024.05
     */
    private String appSecret;

    public ServiceEntry getServiceEntry() {
        return serviceEntry;
    }

    public void setServiceEntry(ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String toString() {
        return "BrokerRegisterReq{"
                + "serviceEntry="
                + serviceEntry
                + ", appKey='"
                + appKey
                + '\''
                + ", appSecret='"
                + appSecret
                + '\''
                + "} "
                + super.toString();
    }
}
