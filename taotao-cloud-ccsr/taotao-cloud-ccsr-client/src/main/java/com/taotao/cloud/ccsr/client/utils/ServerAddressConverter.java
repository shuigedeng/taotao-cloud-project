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

package com.taotao.cloud.ccsr.client.utils;

import com.taotao.cloud.ccsr.client.dto.ServerAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * ServerAddressConverter
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ServerAddressConverter {

    public static List<ServerAddress> convert( List<String> addressStrings ) {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String addr : addressStrings) {
            // 使用 ":" 分割字符串，获取主机和端口
            String[] parts = addr.split(":");
            if (parts.length == 2) {
                String host = parts[0];
                int port = Integer.parseInt(parts[1]);
                // 默认设置 active 为 true
                serverAddresses.add(new ServerAddress(host, port, true));
            } else {
                throw new IllegalArgumentException("Invalid address format: " + addr);
            }
        }
        return serverAddresses;
    }
}
