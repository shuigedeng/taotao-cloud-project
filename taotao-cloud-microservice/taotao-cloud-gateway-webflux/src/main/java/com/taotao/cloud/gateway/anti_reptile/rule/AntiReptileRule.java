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

package com.taotao.cloud.gateway.anti_reptile.rule;

import org.springframework.web.server.ServerWebExchange;

public interface AntiReptileRule {

    /**
     * 反爬规则具体实现
     *
     * @param exchange 请求
     * @return true为击中反爬规则
     */
    boolean execute(ServerWebExchange exchange);

    /**
     * 重置已记录规则
     *
     * @param exchange       请求
     * @param realRequestUri 原始请求uri
     */
    void reset(ServerWebExchange exchange, String realRequestUri);

    /**
     * 规则优先级
     *
     * @return 优先级
     */
    int getOrder();
}
