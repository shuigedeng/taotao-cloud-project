package com.taotao.cloud.workflow.biz.engine.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskNodeEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowNodeEnum;
import com.taotao.cloud.workflow.biz.engine.model.FlowHandleModel;
import com.taotao.cloud.workflow.biz.engine.model.flowbefore.FlowBatchModel;
import com.taotao.cloud.workflow.biz.engine.model.flowbefore.FlowBeforeInfoVO;
import com.taotao.cloud.workflow.biz.engine.model.flowbefore.FlowBeforeListVO;
import com.taotao.cloud.workflow.biz.engine.model.flowbefore.FlowSummary;
import com.taotao.cloud.workflow.biz.engine.model.flowcandidate.FlowCandidateUserModel;
import com.taotao.cloud.workflow.biz.engine.model.flowcandidate.FlowCandidateVO;
import com.taotao.cloud.workflow.biz.engine.model.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.nodejson.ChildNodeList;
import com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.nodejson.ConditionList;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.FlowTaskListModel;
import com.taotao.cloud.workflow.biz.engine.model.flowtask.PaginationFlowTask;
import com.taotao.cloud.workflow.biz.engine.service.FlowEngineService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNodeService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorRecordService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskService;
import com.taotao.cloud.workflow.biz.engine.util.FlowJsonUtil;
import com.taotao.cloud.workflow.biz.engine.util.FlowNature;
import com.taotao.cloud.workflow.biz.engine.util.ServiceAllUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 待我审核
 *
 */
@Tag(tags = "待我审核", value = "FlowBefore")
@RestController
@RequestMapping("/api/workflow/Engine/FlowBefore")
public class FlowBeforeController {


    @Autowired
    private ServiceAllUtil serviceUtil;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private RedisUtil redisUtil;
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

    /**
     * 获取待我审核列表
     *
     * @param category           分类
     * @param paginationFlowTask
     * @return
     */
    @Operation("获取待我审核列表(有带分页)，1-待办事宜，2-已办事宜，3-抄送事宜,4-批量审批")
    @GetMapping("/List/{category}")
    public Result list(@PathVariable("category") String category, PaginationFlowTask paginationFlowTask) {
        List<FlowTaskListModel> data = new ArrayList<>();
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
        List<UserEntity> userList = serviceUtil.getUserName(data.stream().map(t -> t.getCreatorUserId()).collect(Collectors.toList()));
        List<FlowEngineEntity> engineList = flowEngineService.getFlowList(data.stream().map(t -> t.getFlowId()).collect(Collectors.toList()));
        if (data.size() > 0) {
            for (FlowTaskListModel task : data) {
                FlowBeforeListVO vo = JsonUtil.getJsonToBean(task, FlowBeforeListVO.class);
                //用户名称赋值
                UserEntity user = userList.stream().filter(t -> t.getId().equals(vo.getCreatorUserId())).findFirst().orElse(null);
                vo.setUserName(user != null ? user.getRealName() + "/" + user.getAccount() : "");
                FlowEngineEntity engine = engineList.stream().filter(t -> t.getId().equals(vo.getFlowId())).findFirst().orElse(null);
                if (engine != null) {
                    vo.setFormType(engine.getFormType());
                }
                if (isBatch) {
                    ChildNodeList childNode = JsonUtil.getJsonToBean(vo.getApproversProperties(), ChildNodeList.class);
                    vo.setApproversProperties(JsonUtil.getObjectToString(childNode.getProperties()));
                }
                vo.setFlowVersion(StringUtil.isEmpty(vo.getFlowVersion()) ? "" : "v" + vo.getFlowVersion());
                listVO.add(vo);
            }
        }
        PaginationVO paginationVO = JsonUtil.getJsonToBean(paginationFlowTask, PaginationVO.class);
        return Result.page(listVO, paginationVO);
    }

    /**
     * 获取待我审批信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取待我审批信息")
    @GetMapping("/{id}")
    public Result info(@PathVariable("id") String id, String taskNodeId, String taskOperatorId) throws WorkFlowException {
        FlowBeforeInfoVO vo = flowTaskNewService.getBeforeInfo(id, taskNodeId, taskOperatorId);
        return Result.success(vo);
    }

    /**
     * 待我审核审核
     *
     * @param id              待办主键值
     * @param flowHandleModel 流程经办
     * @return
     */
    @Operation("待我审核审核")
    @PostMapping("/Audit/{id}")
    public Result audit(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator == null) {
            return Result.fail("审批失败");
        } else {
            FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
            flowTaskNewService.permissions(operator.getHandleId(), flowTask.getFlowId(), operator, "");
            if (FlowNature.ProcessCompletion.equals(operator.getCompletion())) {
                FlowModel flowModel = JsonUtil.getJsonToBean(flowHandleModel, FlowModel.class);
                UserInfo userInfo = userProvider.get();
                String rejecttKey = userInfo.getTenantId() + id;
                if (redisUtil.exists(rejecttKey)) {
                    throw new WorkFlowException(MsgCode.WF005.get());
                }
                redisUtil.insert(rejecttKey, id, 10);
                flowTaskNewService.audit(flowTask, operator, flowModel);
                return Result.success("审核成功");
            } else {
                return Result.fail("已审核完成");
            }
        }
    }

    /**
     * 保存草稿
     *
     * @param id              待办主键值
     * @param flowHandleModel 流程经办
     * @return
     */
    @Operation("保存草稿")
    @PostMapping("/SaveAudit/{id}")
    public Result saveAudit(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        FlowTaskOperatorEntity flowTaskOperatorEntity = flowTaskOperatorService.getInfo(id);
        if (flowTaskOperatorEntity != null) {
            FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(flowTaskOperatorEntity.getTaskId());
            FlowEngineEntity engine = flowEngineService.getInfo(flowTaskEntity.getFlowId());
            Map<String, Object> formDataAll = flowHandleModel.getFormData();
            if (FlowNature.CUSTOM.equals(engine.getFormType())) {
                Object data = formDataAll.get("data");
                if (data != null) {
                    formDataAll = JsonUtil.stringToMap(String.valueOf(data));
                }
            }
            flowTaskOperatorEntity.setDraftData(JsonUtil.getObjectToString(formDataAll));
            flowTaskOperatorService.updateById(flowTaskOperatorEntity);
            return Result.success(MsgCode.SU002.get());
        }
        return Result.fail(MsgCode.FA001.get());
    }

    /**
     * 审批汇总
     *
     * @param id       待办主键值
     * @param category 类型
     * @return
     */
    @Operation("审批汇总")
    @GetMapping("/RecordList/{id}")
    public Result recordList(@PathVariable("id") String id, String category, String type) {
        List<FlowSummary> flowSummaries = flowTaskNewService.recordList(id, category, type);
        return Result.success(flowSummaries);
    }

    /**
     * 待我审核驳回
     *
     * @param id              待办主键值
     * @param flowHandleModel 经办信息
     * @return
     */
    @Operation("待我审核驳回")
    @PostMapping("/Reject/{id}")
    public Result reject(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator == null) {
            return Result.fail("驳回失败");
        } else {
            FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
            flowTaskNewService.permissions(operator.getHandleId(), flowTask.getFlowId(), operator, "");
            if (FlowNature.ProcessCompletion.equals(operator.getCompletion())) {
                FlowModel flowModel = JsonUtil.getJsonToBean(flowHandleModel, FlowModel.class);
                UserInfo userInfo = userProvider.get();
                String rejecttKey = userInfo.getTenantId() + id;
                if (redisUtil.exists(rejecttKey)) {
                    throw new WorkFlowException(MsgCode.WF112.get());
                }
                redisUtil.insert(rejecttKey, id, 10);
                flowTaskNewService.reject(flowTask, operator, flowModel);
                return Result.success("驳回成功");
            } else {
                return Result.fail("已审核完成");
            }
        }
    }

    /**
     * 待我审核转办
     *
     * @param id              主键值
     * @param flowHandleModel 经办信息
     * @return
     */
    @Operation("待我审核转办")
    @PostMapping("/Transfer/{id}")
    public Result transfer(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator == null) {
            return Result.fail("转办失败");
        } else {
            FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
            flowTaskNewService.permissions(operator.getHandleId(), flowTask.getFlowId(), operator, "");
            operator.setHandleId(flowHandleModel.getFreeApproverUserId());
            flowTaskNewService.transfer(operator);
            return Result.success("转办成功");
        }
    }

    /**
     * 待我审核撤回审核
     * 注意：在撤销流程时要保证你的下一节点没有处理这条记录；如已处理则无法撤销流程。
     *
     * @param id              主键值
     * @param flowHandleModel 实体对象
     * @return
     */
    @Operation("待我审核撤回审核")
    @PostMapping("/Recall/{id}")
    public Result recall(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        FlowTaskOperatorRecordEntity operatorRecord = flowTaskOperatorRecordService.getInfo(id);
        List<FlowTaskNodeEntity> nodeList = flowTaskNodeService.getList(operatorRecord.getTaskId()).stream().filter(t -> FlowNodeEnum.Process.getCode().equals(t.getState())).collect(Collectors.toList());
        FlowTaskNodeEntity taskNode = nodeList.stream().filter(t -> t.getId().equals(operatorRecord.getTaskNodeId())).findFirst().orElse(null);
        if (taskNode != null) {
            FlowModel flowModel = JsonUtil.getJsonToBean(flowHandleModel, FlowModel.class);
            flowTaskNewService.recall(id, operatorRecord, flowModel);
            return Result.success("撤回成功");
        }
        return Result.fail("撤回失败");
    }

    /**
     * 待我审核终止审核
     *
     * @param id              主键值
     * @param flowHandleModel 流程经办
     * @return
     */
    @Operation("待我审核终止审核")
    @PostMapping("/Cancel/{id}")
    public Result cancel(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) {
        FlowTaskEntity flowTaskEntity = flowTaskService.getInfo(id);
        if (flowTaskEntity != null) {
            FlowModel flowModel = JsonUtil.getJsonToBean(flowHandleModel, FlowModel.class);
            flowTaskNewService.cancel(flowTaskEntity, flowModel);
            return Result.success(MsgCode.SU009.get());
        }
        return Result.fail(MsgCode.FA009.get());
    }

    /**
     * 指派人
     *
     * @param id              主键值
     * @param flowHandleModel 流程经办
     * @return
     */
    @Operation("指派人")
    @PostMapping("/Assign/{id}")
    public Result assign(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        FlowModel flowModel = JsonUtil.getJsonToBean(flowHandleModel, FlowModel.class);
        boolean isOk = flowTaskNewService.assign(id, flowModel);
        return isOk ? Result.success("指派成功") : Result.fail("指派失败");
    }

    /**
     * 获取候选人
     *
     * @param flowHandleModel 数据
     * @return
     */
    @Operation("获取候选人节点")
    @PostMapping("/Candidates/{id}")
    public Result candidates(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        List<FlowCandidateVO> candidate = flowTaskNewService.candidates(id, flowHandleModel);
        return Result.success(candidate);
    }

    /**
     * 获取候选人
     *
     * @param flowHandleModel 数据
     * @return
     */
    @Operation("获取候选人")
    @PostMapping("/CandidateUser/{id}")
    public Result candidateUser(@PathVariable("id") String id, @RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        List<FlowCandidateUserModel> candidate = flowTaskNewService.candidateUser(id, flowHandleModel);
        PaginationVO paginationVO = JsonUtil.getJsonToBean(flowHandleModel, PaginationVO.class);
        return Result.page(candidate, paginationVO);
    }

    /**
     * 批量审批引擎
     *
     * @return
     */
    @Operation("批量审批引擎")
    @GetMapping("/BatchFlowSelector")
    public Result batchFlowSelector() {
        List<FlowBatchModel> batchFlowList = flowTaskService.batchFlowSelector();
        return Result.success(batchFlowList);
    }

    /**
     * 引擎节点
     *
     * @param id 主键值
     * @return
     * @throws WorkFlowException
     */
    @Operation("引擎节点")
    @GetMapping("/NodeSelector/{id}")
    public Result nodeSelector(@PathVariable("id") String id) throws WorkFlowException {
        FlowEngineEntity engine = flowEngineService.getInfo(id);
        List<FlowBatchModel> batchList = new ArrayList<>();
        ChildNode childNodeAll = JsonUtil.getJsonToBean(engine.getFlowTemplateJson(), ChildNode.class);
        //获取流程节点
        List<ChildNodeList> nodeListAll = new ArrayList<>();
        List<ConditionList> conditionListAll = new ArrayList<>();
        //递归获取条件数据和节点数据
        FlowJsonUtil.getTemplateAll(childNodeAll, nodeListAll, conditionListAll);
        for (ChildNodeList childNodeList : nodeListAll) {
            FlowBatchModel batchModel = new FlowBatchModel();
            batchModel.setFullName(childNodeList.getProperties().getTitle());
            batchModel.setId(childNodeList.getCustom().getNodeId());
            batchList.add(batchModel);
        }
        return Result.success(batchList);
    }

    /**
     * 批量审批
     *
     * @param flowHandleModel 数据
     * @return
     * @throws WorkFlowException
     */
    @Operation("批量审批")
    @PostMapping("/BatchOperation")
    public Result batchOperation(@RequestBody FlowHandleModel flowHandleModel) throws WorkFlowException {
        flowTaskNewService.batch(flowHandleModel);
        return Result.success("批量操作完成");
    }

    /**
     * 批量获取候选人
     *
     * @param taskOperatorId 代办数据
     * @return
     * @throws WorkFlowException
     */
    @Operation("批量获取候选人")
    @GetMapping("/BatchCandidate")
    public Result batchCandidate(String flowId, String taskOperatorId) throws WorkFlowException {
        List<FlowCandidateVO> candidate = flowTaskNewService.batchCandidates(flowId, taskOperatorId);
        return Result.success(candidate);
    }

    /**
     * 消息跳转工作流
     *
     * @param id 代办id
     * @return
     * @throws WorkFlowException
     */
    @Operation("消息跳转工作流")
    @GetMapping("/{id}/Info")
    public Result taskOperatorId(@PathVariable("id") String id) throws WorkFlowException {
        FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(id);
        if (operator == null) {
            throw new WorkFlowException(MsgCode.WF123.get());
        }
        FlowTaskEntity flowTask = flowTaskService.getInfo(operator.getTaskId());
        flowTaskNewService.permissions(operator.getHandleId(), flowTask.getFlowId(), operator, "");
        return Result.success();
    }


}
