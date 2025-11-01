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

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.*;

import org.springframework.stereotype.Component;

/**
 * 异常时间登录风险实现
 */
@Component
public class UnusualLoginRiskHandle extends AbstractLoginHandle {

    @Override
    public void filterRisk(
            List<RiskRule> filter, Map<Integer, RiskRule> ruleMap, UserAccount account) {
        if (MapUtil.isNotEmpty(ruleMap)) {
            RiskRule loginTimeExe = ruleMap.get(2);
            if (loginTimeExe != null) {
                // 将json转为异常时间对象
                List<UnusualLoginTime> unusualLoginTimes =
                        JSONUtil.toList(loginTimeExe.getUnusualLoginTime(), UnusualLoginTime.class);
                Date now = new Date();
                // 判断当前时间是周几
                int dayOfWeek = DateUtil.dayOfWeek(now);
                for (UnusualLoginTime unusualLoginTime : unusualLoginTimes) {
                    // 如果当前的周数与配置的周数相等，那么判断当前的具体时间
                    if (unusualLoginTime.getWeek() == dayOfWeek) {
                        DateTime startTime =
                                DateUtil.parseTimeToday(unusualLoginTime.getStartTime());
                        DateTime endTime = DateUtil.parseTimeToday(unusualLoginTime.getEndTime());
                        // 如果当前的时间，在配置的时间范围内，那么将算作异常时间登录
                        if (DateUtil.isIn(now, startTime, endTime)) {
                            filter.add(loginTimeExe);
                            break;
                        }
                    }
                }
            }
        }
        // 是否有下一个节点 ， 如果有，继续向下执行
        if (this.nextHandle != null) {
            this.nextHandle.filterRisk(filter, ruleMap, account);
        }
    }

    @Data
    public static class UnusualLoginTime {

        private int week;

        private String startTime;

        private String endTime;
    }
}
