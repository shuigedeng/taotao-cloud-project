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

package com.taotao.cloud.workflow.biz.engine.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.workflow.api.vo.UserEntity;
import com.taotao.cloud.workflow.biz.common.base.vo.PaginationVO;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import com.taotao.cloud.workflow.biz.common.model.engine.FlowHandleModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowBatchModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowBeforeInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowBeforeListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowSummary;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcandidate.FlowCandidateUserModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcandidate.FlowCandidateVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.ChildNode;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ChildNodeList;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ConditionList;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskListModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.PaginationFlowTask;
import com.taotao.cloud.workflow.biz.covert.FlowTaskConvert;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowNodeEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNodeService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorRecordService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.FlowJsonUtil;
import com.taotao.cloud.workflow.biz.engine.util.FlowNature;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;
import com.taotao.cloud.workflow.biz.exception.WorkFlowException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 待我审核 */
@Validated
@Tag(name = "工作流程-待我审核", description = "工作流程-待我审核")
@RestController
@RequestMapping("/api/workflow/engine/flow-before")
public class FlowBeforeController {

    @Autowired
    private ServiceAllUtil serviceUtil;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    @Autowired
    private FlowTaskOperatorRecordService flowTaskOperatorRecordService;

    @Autowired
    private FlowTaskNodeService flowTaskNodeService;

    @Autowired
    private FlowEngineService flowEngineService;

    @Autowired
    private FlowTaskNewService flowTaskNewService;

    @Operation(
            summary = "获取待我审核列表(有带分页)，1-待办事宜，2-已办事宜，3-抄送事宜,4-批量审批",
            description = "获取待我审核列表(有带分页)，1-待办事宜，2-已办事宜，3-抄送事宜,4-批量审批")
    @GetMapping("/category/{category}")
    public Result<PageResult<FlowBeforeListVO>> list(
            @PathVariable("category") String category, PaginationFlowTask paginationFlowTask) {
        IPage<FlowTaskListModel> data = paginationFlowTask.buildMpPage();
        if (FlowNature.WAIT.equals(category)) {
            data = flowTaskService.getWaitList(paginationFlowTask);
        } else if (FlowNature.TRIAL.equals(category)) {
            data = flowTaskService.getTrialList(paginationFlowTask);
        } else if (FlowNature.CIRCULATE.equals(category)) {
            data = flowTaskService.getCirculateList(paginationFlowTask);
        } else if (FlowNature.BATCH.equals(category)) {
            data = flowTaskService.getBatchWaitList(paginationFlowTask);
        }
        boolean isBatch = FlowNature.BATCH.equals(category);

        List<FlowBeforeListVO> listVO = new LinkedList<>();
        List<FlowTaskListModel> records = data.getRecords();
        List<UserEntity> userList = serviceUtil.getUserName(
                records.stream().map(FlowTaskListModel::getCreatorUserId).toList());
        List<FlowEngineEntity> engineList = flowEngineService.getFlowList(
                records.stream().map(FlowTaskListModel::getFlowId).toList());

        if (records.size() > 0) {
            for (FlowTaskListModel task : records) {
                FlowBeforeListVO vo = FlowTaskConvert.INSTANCE.convert(task);

                // 用户名称赋值
                UserEntity user = userList.stream()
                        .filter(t -> t.getId().equals(vo.getCreatorUserId()))
                        .findFirst()
                        .orElse(null);
                vo.setUserName(user != null ? user.getRealName() + "/" + user.getAccount() : "");

                engineList.stream()
                        .filter(t -> t.getId().equals(vo.getFlowId()))
                        .findFirst()
                        .ifPresent(engine -> vo.setFormType(engine.getFormType()));

                if (isBatch) {
                    ChildNodeList childNode = JsonUtils.toObject(vo.getApproversProperties(), ChildNodeList.class);
                    assert childNode != null;
                    vo.setApproversProperties(JsonUtils.toJSONString(childNode.getProperties()));
                }

                vo.setFlowVersion(StringUtils.isEmpty(vo.getFlowVersion()) ? "" : "v" + vo.getFlowVersion());
                listVO.add(vo);
            }
        }
        return Result.success(MpUtils.convertMybatisPage(data, FlowBeforeListVO.class));
    }

    @Operation(summary = "获取待我审批信息", description = "获取待我审批信息")
    @GetMapping("/{id}")
    public Result<FlowBeforeInfoVO> info(@PathVariable("id") String id, String taskNodeId, String taskOperatorId)
            throws WorkFlowException {
        FlowBeforeInfoVO vo = flowTaskNewService.getBeforeInfo(id, taskNodeId, taskOperatorId);
        return Result.success(vo);
    }

    @Operation(summary = "待我审核审核", description = "待我审核审核")
    @PostMapping("/actions/audit/{id}")
    public Result<Boolean> audit(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel)
            throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator == null) {
            return Result.fail("审批失败");
        } else {
            FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
            flowTaskNewService.permissions(operator.getHandleId(), flowTask.getFlowId(), operator, "");
            if (FlowNature.ProcessCompletion.equals(operator.getCompletion())) {
                FlowModel flowModel = FlowTaskConvert.INSTANCE.convert(flowHandleModel);
                String rejecttKey = SecurityUtils.getTenant() + id;
                if (redisRepository.exists(rejecttKey)) {
                    throw new WorkFlowException(MsgCode.WF005.get());
                }
                redisRepository.setExpire(rejecttKey, id, 10);
                flowTaskNewService.audit(flowTask, operator, flowModel);
                return Result.success(true);
            } else {
                return Result.fail("已审核完成");
            }
        }
    }

    @Operation(summary = "保存草稿", description = "保存草稿")
    @PostMapping("/actions/audit/{id}")
    public Result<Boolean> saveAudit(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel)
            throws WorkFlowException {
        FlowTaskOperatorEntity flowTaskOperatorEntity = flowTaskOperatorService.getInfo(id);
        if (flowTaskOperatorEntity != null) {
            FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(flowTaskOperatorEntity.getTaskId());
            FlowEngineEntity engine = flowEngineService.getInfo(flowTaskEntity.getFlowId());
            Map<String, Object> formDataAll = flowHandleModel.getFormData();
            if (FlowNature.CUSTOM.equals(engine.getFormType())) {
                Object data = formDataAll.get("data");
                if (data != null) {
                    formDataAll = JsonUtils.toMap(String.valueOf(data));
                }
            }
            flowTaskOperatorEntity.setDraftData(JsonUtils.toJSONString(formDataAll));
            flowTaskOperatorService.updateById(flowTaskOperatorEntity);
            return Result.success(true);
        }
        return Result.fail(MsgCode.FA001.get());
    }

    @Operation(summary = "审批汇总", description = "审批汇总")
    @GetMapping("/records/{id}")
    public Result<List<FlowSummary>> recordList(@PathVariable("id") String id, String category, String type) {
        List<FlowSummary> flowSummaries = flowTaskNewService.recordList(id, category, type);
        return Result.success(flowSummaries);
    }

    @Operation(summary = "待我审核驳回", description = "待我审核驳回")
    @PostMapping("/actions/reject/{id}")
    public Result<Boolean> reject(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel)
            throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator == null) {
            return Result.fail("驳回失败");
        } else {
            FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
            flowTaskNewService.permissions(operator.getHandleId(), flowTask.getFlowId(), operator, "");
            if (FlowNature.ProcessCompletion.equals(operator.getCompletion())) {
                FlowModel flowModel = FlowTaskConvert.INSTANCE.convert(flowHandleModel);
                String rejecttKey = SecurityUtils.getTenant() + id;
                if (redisRepository.exists(rejecttKey)) {
                    throw new WorkFlowException(MsgCode.WF112.get());
                }
                redisRepository.setExpire(rejecttKey, id, 10);
                flowTaskNewService.reject(flowTask, operator, flowModel);
                return Result.success(true);
            } else {
                return Result.fail("已审核完成");
            }
        }
    }

    @Operation(summary = "待我审核转办", description = "待我审核转办")
    @PostMapping("/actions/transfer/{id}")
    public Result<Boolean> transfer(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel)
            throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator == null) {
            return Result.fail("转办失败");
        } else {
            FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
            flowTaskNewService.permissions(operator.getHandleId(), flowTask.getFlowId(), operator, "");
            operator.setHandleId(flowHandleModel.getFreeApproverUserId());
            flowTaskNewService.transfer(operator);
            return Result.success(true);
        }
    }

    @Operation(summary = "待我审核撤回审核", description = "注意：在撤销流程时要保证你的下一节点没有处理这条记录；如已处理则无法撤销流程。")
    @PostMapping("/actions/recall/{id}")
    public Result<Boolean> recall(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel)
            throws WorkFlowException {
        FlowTaskOperatorRecordEntity operatorRecord = flowTaskOperatorRecordService.getInfo(id);
        List<FlowTaskNodeEntity> nodeList = flowTaskNodeService.getList(operatorRecord.getTaskId()).stream()
                .filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState()))
                .toList();
        FlowTaskNodeEntity taskNode = nodeList.stream()
                .filter(t -> t.getId().equals(operatorRecord.getTaskNodeId()))
                .findFirst()
                .orElse(null);
        if (taskNode != null) {
            FlowModel flowModel = FlowTaskConvert.INSTANCE.convert(flowHandleModel);
            flowTaskNewService.recall(id, operatorRecord, flowModel);
            return Result.success(true);
        }
        return Result.fail("撤回失败");
    }

    @Operation(summary = "待我审核终止审核", description = "待我审核终止审核")
    @PostMapping("/actions/cancel/{id}")
    public Result<Boolean> cancel(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) {
        FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(id);
        if (flowTaskEntity != null) {
            FlowModel flowModel = FlowTaskConvert.INSTANCE.convert(flowHandleModel);
            flowTaskNewService.cancel(flowTaskEntity, flowModel);
            return Result.success(true);
        }
        return Result.fail(MsgCode.FA009.get());
    }

    @Operation(summary = "指派人", description = "指派人")
    @PostMapping("/actions/assign/{id}")
    public Result<Boolean> assign(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel)
            throws WorkFlowException {
        FlowModel flowModel = FlowTaskConvert.INSTANCE.convert(flowHandleModel);
        boolean isOk = flowTaskNewService.assign(id, flowModel);
        return isOk ? Result.success(true) : Result.fail("指派失败");
    }

    @Operation(summary = "获取候选人节点", description = "获取候选人节点")
    @PostMapping("/actions/candidates/{id}")
    public Result<List<FlowCandidateVO>> candidates(
            @PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        List<FlowCandidateVO> candidate = flowTaskNewService.candidates(id, flowHandleModel);
        return Result.success(candidate);
    }

    @Operation(summary = "获取候选人", description = "获取候选人")
    @PostMapping("/CandidateUser/{id}")
    public Result<PageResult<FlowCandidateUserModel>> candidateUser(
            @PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        List<FlowCandidateUserModel> candidate = flowTaskNewService.candidateUser(id, flowHandleModel);
        PaginationVO paginationVO = JsonUtils.getJsonToBean(flowHandleModel, PaginationVO.class);
        return Result.page(candidate, paginationVO);
    }

    @Operation(summary = "批量审批引擎", description = "批量审批引擎")
    @GetMapping("/batch")
    public Result<List<FlowBatchModel>> batchFlowSelector() {
        List<FlowBatchModel> batchFlowList = flowTaskService.batchFlowSelector();
        return Result.success(batchFlowList);
    }

    @Operation(summary = "引擎节点", description = "引擎节点")
    @GetMapping("/node/{id}")
    public Result<List<FlowBatchModel>> nodeSelector(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity engine = flowEngineService.getInfo(id);
        List<FlowBatchModel> batchList = new ArrayList<>();
        ChildNode childNodeAll = JsonUtils.toObject(engine.getFlowTemplateJson(), ChildNode.class);
        // 获取流程节点
        List<ChildNodeList> nodeListAll = new ArrayList<>();
        List<ConditionList> conditionListAll = new ArrayList<>();
        // 递归获取条件数据和节点数据
        FlowJsonUtil.getTemplateAll(childNodeAll, nodeListAll, conditionListAll);
        for (ChildNodeList childNodeList : nodeListAll) {
            FlowBatchModel batchModel = new FlowBatchModel();
            batchModel.setFullName(childNodeList.getProperties().getTitle());
            batchModel.setId(childNodeList.getCustom().getNodeId());
            batchList.add(batchModel);
        }
        return Result.success(batchList);
    }

    @Operation(summary = "批量审批", description = "批量审批")
    @PostMapping("/batch")
    public Result<Boolean> batchOperation(@RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        flowTaskNewService.batch(flowHandleModel);
        return Result.success(true);
    }

    @Operation(summary = "批量获取候选人", description = "批量获取候选人")
    @GetMapping("/batch/candidate")
    public Result<List<FlowCandidateVO>> batchCandidate(String flowId, String taskOperatorId) throws WorkFlowException {
        List<FlowCandidateVO> candidate = flowTaskNewService.batchCandidates(flowId, taskOperatorId);
        return Result.success(candidate);
    }

    @Operation(summary = "消息跳转工作流", description = "消息跳转工作流")
    @GetMapping("/info/{id}")
    public Result<FlowTaskEntity> taskOperatorId(@PathVariable("id") String id) throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator == null) {
            throw new WorkFlowException(MsgCode.WF123.get());
        }
        FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
        flowTaskNewService.permissions(operator.getHandleId(), flowTask.getFlowId(), operator, "");
        return Result.success(flowTask);
    }
}
