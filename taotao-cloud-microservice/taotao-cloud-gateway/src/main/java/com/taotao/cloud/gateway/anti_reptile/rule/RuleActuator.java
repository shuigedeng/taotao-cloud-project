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

import java.util.List;

import org.springframework.web.server.ServerWebExchange;

/**
 * RuleActuator
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class RuleActuator {

    private List<AntiReptileRule> ruleList;

    public RuleActuator( List<AntiReptileRule> rules ) {
        ruleList = rules;
    }

    /**
     * 是否允许通过请求
     *
     * @param exchange 请求
     * @return 请求是否允许通过
     */
    public boolean isAllowed( ServerWebExchange exchange ) {
        for (AntiReptileRule rule : ruleList) {
            if (rule.execute(exchange)) {
                return false;
            }
        }
        return true;
    }

    public void reset( ServerWebExchange exchange, String realRequestUri ) {
        ruleList.forEach(rule -> rule.reset(exchange, realRequestUri));
    }
}
