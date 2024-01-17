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

package com.taotao.cloud.workflow.biz.flowable.bpm.service.definition;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionListReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionPageReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import com.taotao.cloud.flowable.biz.bpm.service.definition.dto.BpmProcessDefinitionCreateReqDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.validation.Valid;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * Flowable流程定义接口
 *
 * @author yunlong.li
 * @author ZJQ
 * @author 芋道源码
 */
public interface BpmProcessDefinitionService {

    /**
     * 获得流程定义分页
     *
     * @param pageReqVO 分页入参
     * @return 流程定义 Page
     */
    PageResult<BpmProcessDefinitionPageItemRespVO> getProcessDefinitionPage(BpmProcessDefinitionPageReqVO pageReqVO);

    /**
     * 获得流程定义列表
     *
     * @param listReqVO 列表入参
     * @return 流程定义列表
     */
    List<BpmProcessDefinitionRespVO> getProcessDefinitionList(BpmProcessDefinitionListReqVO listReqVO);

    /**
     * 创建流程定义
     *
     * @param createReqDTO 创建信息
     * @return 流程编号
     */
    String createProcessDefinition(@Valid BpmProcessDefinitionCreateReqDTO createReqDTO);

    /**
     * 更新流程定义状态
     *
     * @param id 流程定义的编号
     * @param state 状态
     */
    void updateProcessDefinitionState(String id, Integer state);

    /**
     * 获得流程定义对应的 BPMN XML
     *
     * @param id 流程定义编号
     * @return BPMN XML
     */
    String getProcessDefinitionBpmnXML(String id);

    /**
     * 获得需要创建的流程定义，是否和当前激活的流程定义相等
     *
     * @param createReqDTO 创建信息
     * @return 是否相等
     */
    boolean isProcessDefinitionEquals(@Valid BpmProcessDefinitionCreateReqDTO createReqDTO);

    /**
     * 获得编号对应的 BpmProcessDefinitionExtDO
     *
     * @param id 编号
     * @return 流程定义拓展
     */
    BpmProcessDefinitionExtDO getProcessDefinitionExt(String id);

    /**
     * 获得编号对应的 ProcessDefinition
     *
     * @param id 编号
     * @return 流程定义
     */
    ProcessDefinition getProcessDefinition(String id);

    /**
     * 获得编号对应的 ProcessDefinition
     *
     * <p>相比 {@link #getProcessDefinition(String)} 方法，category 的取值是正确
     *
     * @param id 编号
     * @return 流程定义
     */
    ProcessDefinition getProcessDefinition2(String id);

    /**
     * 获得 deploymentId 对应的 ProcessDefinition
     *
     * @param deploymentId 部署编号
     * @return 流程定义
     */
    ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId);

    /**
     * 获得 deploymentIds 对应的 ProcessDefinition 数组
     *
     * @param deploymentIds 部署编号的数组
     * @return 流程定义的数组
     */
    List<ProcessDefinition> getProcessDefinitionListByDeploymentIds(Set<String> deploymentIds);

    /**
     * 获得流程定义标识对应的激活的流程定义
     *
     * @param key 流程定义的标识
     * @return 流程定义
     */
    ProcessDefinition getActiveProcessDefinition(String key);

    /**
     * 获得 ids 对应的 Deployment Map
     *
     * @param ids 部署编号的数组
     * @return 流程部署 Map
     */
    default Map<String, Deployment> getDeploymentMap(Set<String> ids) {
        return CollectionUtils.convertMap(getDeployments(ids), Deployment::getId);
    }

    /**
     * 获得 ids 对应的 Deployment 数组
     *
     * @param ids 部署编号的数组
     * @return 流程部署的数组
     */
    List<Deployment> getDeployments(Set<String> ids);

    /**
     * 获得 id 对应的 Deployment
     *
     * @param id 部署编号
     * @return 流程部署
     */
    Deployment getDeployment(String id);

    /**
     * 获得 Bpmn 模型
     *
     * @param processDefinitionId 流程定义的编号
     * @return Bpmn 模型
     */
    BpmnModel getBpmnModel(String processDefinitionId);
}
