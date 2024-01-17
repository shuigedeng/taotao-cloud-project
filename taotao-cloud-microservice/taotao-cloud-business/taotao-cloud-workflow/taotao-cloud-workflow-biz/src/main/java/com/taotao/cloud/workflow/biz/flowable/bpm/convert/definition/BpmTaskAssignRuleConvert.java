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

package com.taotao.cloud.workflow.biz.flowable.bpm.convert.definition;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleRespVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.definition.BpmTaskAssignRuleDO;
import java.util.List;
import java.util.Map;
import org.flowable.bpmn.model.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BpmTaskAssignRuleConvert {
    BpmTaskAssignRuleConvert INSTANCE = Mappers.getMapper(BpmTaskAssignRuleConvert.class);

    default List<BpmTaskAssignRuleRespVO> convertList(List<UserTask> tasks, List<BpmTaskAssignRuleDO> rules) {
        Map<String, BpmTaskAssignRuleDO> ruleMap =
                CollectionUtils.convertMap(rules, BpmTaskAssignRuleDO::getTaskDefinitionKey);
        // 以 UserTask 为主维度，原因是：流程图编辑后，一些规则实际就没用了。
        return CollectionUtils.convertList(tasks, task -> {
            BpmTaskAssignRuleRespVO respVO = convert(ruleMap.get(task.getId()));
            if (respVO == null) {
                respVO = new BpmTaskAssignRuleRespVO();
                respVO.setTaskDefinitionKey(task.getId());
            }
            respVO.setTaskDefinitionName(task.getName());
            return respVO;
        });
    }

    BpmTaskAssignRuleRespVO convert(BpmTaskAssignRuleDO bean);

    BpmTaskAssignRuleDO convert(BpmTaskAssignRuleCreateReqVO bean);

    BpmTaskAssignRuleDO convert(BpmTaskAssignRuleUpdateReqVO bean);

    List<BpmTaskAssignRuleDO> convertList2(List<BpmTaskAssignRuleRespVO> list);
}
