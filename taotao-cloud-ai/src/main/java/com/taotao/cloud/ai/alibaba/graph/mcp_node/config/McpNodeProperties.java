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

package com.taotao.cloud.ai.alibaba.graph.mcp_node.config;

import java.util.Map;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yingzi
 * @since 2025/6/14
 */
@ConfigurationProperties(prefix = McpNodeProperties.PREFIX)
public class McpNodeProperties {

    public static final String PREFIX = "spring.ai.graph.nodes";

    private Map<String, Set<String>> node2servers;

    public Map<String, Set<String>> getNode2servers() {
        return node2servers;
    }

    public void setNode2servers(Map<String, Set<String>> node2servers) {
        this.node2servers = node2servers;
    }
}
