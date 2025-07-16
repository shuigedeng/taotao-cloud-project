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

package com.taotao.cloud.auth.biz.management.compliance.processor.loginrisk;

import java.util.List;
import java.util.Map;

/**
 * 登录风险处理抽象父类
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-07 09:24:54
 */
public abstract class AbstractLoginHandle {

    public AbstractLoginHandle nextHandle; // 下一个执行节点

    public void setNextHandle(AbstractLoginHandle nextHandle) {
        this.nextHandle = nextHandle;
    }

    /**
     * 具体的执行方法，过滤出满足风控的规则
     *
     * @param filter  满足风控的规则
     * @param ruleMap 所有规则集合
     * @param account 登录账户
     */
    public abstract void filterRisk(
            List<RiskRule> filter, Map<Integer, RiskRule> ruleMap, UserAccount account);
}
