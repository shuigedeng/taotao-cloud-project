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

package com.taotao.cloud.ccsr.client.option;

import com.taotao.cloud.ccsr.client.dto.ServerAddress;
import com.taotao.cloud.ccsr.client.utils.ServerAddressConverter;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

/**
 * GrpcOption
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GrpcOption extends RequestOption {

    private List<ServerAddress> serverAddresses;

    @Override
    public String protocol() {
        return "grpc";
    }

    public void initServers( List<String> addresses ) {
        this.serverAddresses = ServerAddressConverter.convert(addresses);
        if (CollectionUtils.isEmpty(serverAddresses)) {
            throw new IllegalArgumentException("Invalid params: the server address is empty");
        }
    }
}
