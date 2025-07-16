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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.stereotype.Component;

/**
 * 登录ip风险实现
 */
@Component
public class IPRiskHandle extends AbstractLoginHandle {

    @Override
    public void filterRisk(
            List<RiskRule> filter, Map<Integer, RiskRule> ruleMap, UserAccount account) {
        if (MapUtil.isNotEmpty(ruleMap)) {
            RiskRule ipRisk = ruleMap.get(3);
            // 判断是否配置登录ip白名单
            if (null != ipRisk && StrUtil.isNotEmpty(ipRisk.getAcceptIp())) {
                List<String> acceptIpList = Arrays.asList(ipRisk.getAcceptIp().split(","));
                // 当前登录ip是否在白名单内，如果不在，则添加到filter中
                if (!acceptIpList.contains(account.getIp())) {
                    filter.add(ipRisk);
                }
            }
        }
        if (this.nextHandle != null) {
            this.nextHandle.filterRisk(filter, ruleMap, account);
        }
    }
}
