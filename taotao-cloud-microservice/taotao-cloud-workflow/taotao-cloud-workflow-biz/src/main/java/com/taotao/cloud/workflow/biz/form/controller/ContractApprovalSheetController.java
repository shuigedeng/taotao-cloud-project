package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.base.util.RegexUtils;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.ContractApprovalSheetEntity;
import jnpf.form.model.contractapprovalsheet.ContractApprovalSheetForm;
import jnpf.form.model.contractapprovalsheet.ContractApprovalSheetInfoVO;
import jnpf.form.service.ContractApprovalSheetService;
import jnpf.util.JsonUtil;
import jnpf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 合同申请单表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "合同申请单表", value = "ContractApprovalSheet")
@RestController
@RequestMapping("/api/workflow/Form/ContractApprovalSheet")
public class ContractApprovalSheetController {

    @Autowired
    private ContractApprovalSheetService contractApprovalSheetService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取合同申请单表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取合同申请单表信息")
    @GetMapping("/{id}")
    public ActionResult<ContractApprovalSheetInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ContractApprovalSheetInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ContractApprovalSheetInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ContractApprovalSheetEntity entity = contractApprovalSheetService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ContractApprovalSheetInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建合同申请单表
     *
     * @param contractApprovalSheetForm 表单对象
     * @return
     */
    @ApiOperation("新建合同申请单表")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ContractApprovalSheetForm contractApprovalSheetForm) throws WorkFlowException {
        if (contractApprovalSheetForm.getStartContractDate() > contractApprovalSheetForm.getEndContractDate()) {
            return ActionResult.fail("结束时间不能小于开始时间");
        }
        if (contractApprovalSheetForm.getIncomeAmount() != null && !"".equals(String.valueOf(contractApprovalSheetForm.getIncomeAmount())) && !RegexUtils.checkDecimals2(String.valueOf(contractApprovalSheetForm.getIncomeAmount()))) {
            return ActionResult.fail("收入金额必须大于0，最多可以输入两位小数点");
        }
        if (contractApprovalSheetForm.getTotalExpenditure() != null && !"".equals(String.valueOf(contractApprovalSheetForm.getIncomeAmount())) && !RegexUtils.checkDecimals2(String.valueOf(contractApprovalSheetForm.getTotalExpenditure()))) {
            return ActionResult.fail("支出金额必须大于0，最多可以输入两位小数点");
        }
        ContractApprovalSheetEntity entity = JsonUtil.getJsonToBean(contractApprovalSheetForm, ContractApprovalSheetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(contractApprovalSheetForm.getStatus())) {
            contractApprovalSheetService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        contractApprovalSheetService.submit(entity.getId(), entity, contractApprovalSheetForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改合同申请单表
     *
     * @param contractApprovalSheetForm 表单对象
     * @param id                        主键
     * @return
     */
    @ApiOperation("修改合同申请单表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ContractApprovalSheetForm contractApprovalSheetForm, @PathVariable("id") String id) throws WorkFlowException {
        if (contractApprovalSheetForm.getStartContractDate() > contractApprovalSheetForm.getEndContractDate()) {
            return ActionResult.fail("结束时间不能小于开始时间");
        }
        if (contractApprovalSheetForm.getIncomeAmount() != null && !"".equals(String.valueOf(contractApprovalSheetForm.getIncomeAmount())) && !RegexUtils.checkDecimals2(String.valueOf(contractApprovalSheetForm.getIncomeAmount()))) {
            return ActionResult.fail("收入金额必须大于0，最多可以输入两位小数点");
        }
        if (contractApprovalSheetForm.getTotalExpenditure() != null && !"".equals(String.valueOf(contractApprovalSheetForm.getTotalExpenditure())) && !RegexUtils.checkDecimals2(String.valueOf(contractApprovalSheetForm.getTotalExpenditure()))) {
            return ActionResult.fail("支出金额必须大于0，最多可以输入两位小数点");
        }
        ContractApprovalSheetEntity entity = JsonUtil.getJsonToBean(contractApprovalSheetForm, ContractApprovalSheetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(contractApprovalSheetForm.getStatus())) {
            contractApprovalSheetService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        contractApprovalSheetService.submit(id, entity, contractApprovalSheetForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
