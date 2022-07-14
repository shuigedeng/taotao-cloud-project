package com.taotao.cloud.workflow.biz.form.controller;


import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 合同审批
 */
@Tag(tags = "合同审批", value = "ContractApproval")
@RestController
@RequestMapping("/api/workflow/Form/ContractApproval")
public class ContractApprovalController {

    @Autowired
    private ContractApprovalService contractApprovalService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取合同审批信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取合同审批信息")
    @GetMapping("/{id}")
    public ActionResult<ContractApprovalInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ContractApprovalInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ContractApprovalInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ContractApprovalEntity entity = contractApprovalService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ContractApprovalInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建合同审批
     *
     * @param contractApprovalForm 表单对象
     * @return
     */
    @Operation("新建合同审批")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ContractApprovalForm contractApprovalForm) throws WorkFlowException {
        ContractApprovalEntity entity = JsonUtil.getJsonToBean(contractApprovalForm, ContractApprovalEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(contractApprovalForm.getStatus())) {
            contractApprovalService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        contractApprovalService.submit(entity.getId(), entity, contractApprovalForm.getFreeApproverUserId(), contractApprovalForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改合同审批
     *
     * @param contractApprovalForm 表单对象
     * @param id                   主键
     * @return
     */
    @Operation("修改合同审批")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ContractApprovalForm contractApprovalForm, @PathVariable("id") String id) throws WorkFlowException {
        ContractApprovalEntity entity = JsonUtil.getJsonToBean(contractApprovalForm, ContractApprovalEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(contractApprovalForm.getStatus())) {
            contractApprovalService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        contractApprovalService.submit(id, entity, contractApprovalForm.getFreeApproverUserId(), contractApprovalForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
