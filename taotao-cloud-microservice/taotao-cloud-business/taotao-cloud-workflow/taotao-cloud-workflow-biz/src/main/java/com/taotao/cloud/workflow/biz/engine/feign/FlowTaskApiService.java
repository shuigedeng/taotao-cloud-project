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

package com.taotao.cloud.workflow.biz.engine.feign;

import com.taotao.cloud.workflow.api.feign.flow.FlowTaskApi;
import com.taotao.cloud.workflow.biz.common.base.ActionResult;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskListModel;
import com.taotao.cloud.workflow.biz.common.util.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import com.taotao.cloud.workflow.biz.engine.service.FlowDynamicService;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNodeService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** 流程引擎 */
@Api(tags = "流程引擎", value = "FlowTask")
@Service
public class FlowTaskApiService implements FlowTaskApi {

    @Autowired
    private FlowDynamicService flowDynamicService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private FlowTaskNewService flowTaskNewService;

    @Autowired
    private FlowTaskNodeService flowTaskNodeService;

    @Autowired
    private FlowEngineService flowEngineService;

    @Autowired
    private FlowDelegateService flowDelegateService;

    /**
     * 动态表单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("动态表单信息")
    @GetMapping("/{id}")
    public ActionResult dataInfo(@PathVariable("id") String id, String taskOperatorId) throws WorkFlowException {
        FlowTaskEntity entity = flowTaskService.getInfo(id);
        FlowTaskInfoVO vo = flowDynamicService.info(entity, taskOperatorId);
        return ActionResult.success(vo);
    }

    /**
     * 保存
     *
     * @param flowTaskForm 动态表单
     * @return
     */
    @ApiOperation("保存")
    @PostMapping
    public ActionResult save(@RequestBody FlowTaskForm flowTaskForm) throws WorkFlowException {
        if (FlowStatusEnum.save.getMessage().equals(flowTaskForm.getStatus())) {
            flowDynamicService.save(null, flowTaskForm);
            return ActionResult.success(MsgCode.SU002.get());
        }
        flowDynamicService.submit(null, flowTaskForm);
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 提交
     *
     * @param flowTaskForm 动态表单
     * @return
     */
    @ApiOperation("提交")
    @PutMapping("/{id}")
    public ActionResult submit(@RequestBody FlowTaskForm flowTaskForm, @PathVariable("id") String id)
            throws WorkFlowException {
        if (FlowStatusEnum.save.getMessage().equals(flowTaskForm.getStatus())) {
            flowDynamicService.save(id, flowTaskForm);
            return ActionResult.success(MsgCode.SU002.get());
        }
        flowDynamicService.submit(id, flowTaskForm);
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 动态表单详情
     *
     * @param flowId 引擎主键值
     * @param id 主键值
     * @return
     */
    @ApiOperation("动态表单信息")
    @GetMapping("/{flowId}/{id}")
    public ActionResult info(@PathVariable("flowId") String flowId, @PathVariable("id") String id)
            throws WorkFlowException {
        Map<String, Object> data = flowDynamicService.getData(flowId, id);
        return ActionResult.success(data);
    }

    // ———————————————内部使用接口——————————

    /**
     * 列表（待我审批）
     *
     * @return
     */
    @Override
    @GetMapping("/GetWaitList")
    public List<FlowTaskEntity> getWaitList() {
        return flowTaskService.getWaitList();
    }

    /**
     * 列表（我已审批）
     *
     * @return
     */
    @Override
    @GetMapping("/GetTrialList")
    public List<FlowTaskListModel> getTrialList() {
        return flowTaskService.getTrialList();
    }

    /**
     * 列表（待我审批）
     *
     * @return
     */
    @Override
    @GetMapping("/GetAllWaitList")
    public List<FlowTaskListModel> getAllWaitList() {
        return flowTaskService.getDashboardAllWaitList();
    }

    /**
     * 列表（订单状态）
     *
     * @return
     */
    @Override
    @GetMapping("/GetOrderStaList/{idAll}")
    public List<FlowTaskEntity> getOrderStaList(@PathVariable("idAll") String idAll) {
        List<String> id = Arrays.asList(idAll.split(","));
        return flowTaskService.getOrderStaList(id);
    }

    /**
     * 提交
     *
     * @param flowSumbitModel
     * @throws WorkFlowException
     */
    @Override
    @PostMapping("/Submit")
    public void submit(@RequestBody FlowSumbitModel flowSumbitModel) throws WorkFlowException {
        FlowModel flowModel = JsonUtil.getJsonToBean(flowSumbitModel, FlowModel.class);
        flowModel.setFormEntity(flowSumbitModel.getFormEntity());
        flowTaskNewService.submit(flowModel);
    }

    /**
     * 撤回
     *
     * @param flowRevokeModel
     * @throws WorkFlowException
     */
    @Override
    @PostMapping("/Revoke")
    public void revoke(@RequestBody FlowRevokeModel flowRevokeModel) throws WorkFlowException {
        FlowModel flowModel = JsonUtil.getJsonToBean(flowRevokeModel.getFlowHandleModel(), FlowModel.class);
        flowTaskNewService.revoke(flowRevokeModel.getFlowTaskEntity(), flowModel);
    }

    /**
     * 流程节点
     *
     * @param id
     * @return
     */
    @Override
    @GetMapping("/getList/{id}")
    public List<FlowTaskNodeEntity> getList(@PathVariable("id") String id) {
        return flowTaskNodeService.getList(id);
    }

    /**
     * 获取流程引擎实体
     *
     * @param id
     * @return
     * @throws WorkFlowException
     */
    @Override
    @PostMapping("/getFlowEngine/{id}")
    public FlowEngineEntity getInfo(@PathVariable("id") String id) throws WorkFlowException {
        return flowEngineService.getInfo(id);
    }

    /**
     * 获取流程引擎实体
     *
     * @param id
     * @return
     */
    @Override
    @PostMapping("/getEngineInfo/{id}")
    public FlowEngineEntity getEngineInfo(@PathVariable("id") String id) {
        return flowEngineService.getById(id);
    }

    /**
     * 更新流程引擎
     *
     * @param id
     * @param engineEntity
     * @throws WorkFlowException
     */
    @Override
    @PostMapping("/updateFlowEngine/{id}")
    public void update(@PathVariable("id") String id, @RequestBody FlowEngineEntity engineEntity)
            throws WorkFlowException {
        flowEngineService.update(id, engineEntity);
    }

    /** 删除流程引擎 */
    @Override
    @PostMapping("/deleteFlowEngine")
    public void delete(@RequestBody FlowEngineEntity engineEntity) throws WorkFlowException {
        flowEngineService.delete(engineEntity);
    }

    /**
     * 创建流程任务
     *
     * @param id
     * @return
     */
    @Override
    @PostMapping("/createFlowTask/{id}")
    public FlowTaskEntity getInfoSubmit(@PathVariable("id") String id) {
        return flowTaskService.getInfoSubmit(id);
    }

    @Override
    @PostMapping("/deleteFlowTask")
    public void deleteFlowTask(@RequestBody FlowTaskEntity taskEntity) throws WorkFlowException {
        flowTaskService.delete(taskEntity);
    }

    @Override
    @GetMapping("/getInfoByEnCode/{encode}")
    public FlowEngineEntity getInfoByEnCode(@PathVariable("encode") String encode) throws WorkFlowException {
        return flowEngineService.getInfoByEnCode(encode);
    }

    @Override
    @GetMapping("/getFlowFormList")
    public List<FlowEngineEntity> getFlowFormList() {
        return flowEngineService.getFlowFormList();
    }

    @Override
    @GetMapping("/getDelegateList")
    public List<FlowDelegateEntity> getDelegateList() {
        return flowDelegateService.getList();
    }
}
