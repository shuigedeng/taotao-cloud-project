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

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * 使用责任链模式实现登录风险控制
 *
 * @Resource private LoginHandleManage loginHandleManage;
 * <p>
 * public String login(UserAccount account) throws Exception {
 * //执行责任链
 * loginHandleManage.execute(account);
 * //TODO 登录逻辑
 * String token = "";
 * return token;
 * }
 */
@Slf4j
@Component
public class LoginHandleManage {

    // @Resource
    // private RiskRuleService riskRuleService;
    //
    // @Resource
    // private LoginLogService loginLogService;

    @Resource private IPRiskHandle ipRiskHandle;

    @Resource private LoginAreaRiskHandle loginAreaRiskHandle;

    @Resource private PasswordErrorRiskHandle passwordErrorRiskHandle;

    @Resource private UnusualLoginRiskHandle unusualLoginRiskHandle;

    /**
     * 构建执行顺序
     * passwordErrorRiskHandle -> unusualLoginRiskHandle -> ipRiskHandle -> loginAreaRiskHandle
     */
    @PostConstruct
    public void init() {
        passwordErrorRiskHandle.setNextHandle(unusualLoginRiskHandle);
        unusualLoginRiskHandle.setNextHandle(ipRiskHandle);
        ipRiskHandle.setNextHandle(loginAreaRiskHandle);
    }

    /**
     * 执行链路入口
     *
     * @param account
     * @throws Exception
     */
    public void execute(UserAccount account) throws Exception {
        // 获取所有风险规则
        // List<RiskRule> riskRules = riskRuleService.lambdaQuery().list();
        List<RiskRule> riskRules = new ArrayList<>();
        Map<Integer, RiskRule> riskRuleMap =
                riskRules.stream().collect(Collectors.toMap(RiskRule::getId, r -> r));
        List<RiskRule> filterRisk = new ArrayList<>();
        // 开始从首节点执行
        passwordErrorRiskHandle.filterRisk(filterRisk, riskRuleMap, account);

        if (CollUtil.isNotEmpty(filterRisk)) {
            // 获取最严重处置措施的规则
            Optional<RiskRule> optional =
                    filterRisk.stream().max(Comparator.comparing(RiskRule::getOperate));
            if (optional.isPresent()) {
                RiskRule riskRule = optional.get();
                handleOperate(riskRule); // 处置

                // TODO 记录日志

            }
        }
    }

    /**
     * 处置风险
     *
     * @param riskRule
     * @throws Exception
     */
    public void handleOperate(RiskRule riskRule) throws Exception {
        int operate = riskRule.getOperate().intValue();
        // if (operate == OperateEnum.TIP.op) { //1
        //	log.info("========执行提示逻辑========");
        // } else if (operate == OperateEnum.SMS.op) {//2
        //	log.info("========执行短信提醒逻辑========");
        // } else if (operate == OperateEnum.BLOCK.op) {//3
        //	log.info("========执行登录阻断逻辑========");
        //	throw new Exception("登录存在风险！");
        // } else if (operate == OperateEnum.DISABLE.op) {//4
        //	log.info("========执行封号逻辑========");
        //	throw new Exception("登录存在风险，账号被封！");
        // }
    }
}
