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
import jnpf.form.entity.ConBillingEntity;
import jnpf.form.model.conbilling.ConBillingForm;
import jnpf.form.model.conbilling.ConBillingInfoVO;
import jnpf.form.service.ConBillingService;
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
 * 合同开票流程
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "合同开票流程", value = "ConBilling")
@RestController
@RequestMapping("/api/workflow/Form/ConBilling")
public class ConBillingController {

    @Autowired
    private ConBillingService conBillingService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取合同开票流程信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取合同开票流程信息")
    @GetMapping("/{id}")
    public ActionResult<ConBillingInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ConBillingInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ConBillingInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ConBillingEntity entity = conBillingService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ConBillingInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建合同开票流程
     *
     * @param conBillingForm 表单对象
     * @return
     */
    @ApiOperation("新建合同开票流程")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ConBillingForm conBillingForm) throws WorkFlowException {
        if (conBillingForm.getBillAmount() != null && !"".equals(String.valueOf(conBillingForm.getBillAmount())) && !RegexUtils.checkDecimals2(String.valueOf(conBillingForm.getBillAmount()))) {
            return ActionResult.fail("开票金额必须大于0，最多可以精确到小数点后两位");
        }
        if (conBillingForm.getPayAmount() != null && !"".equals(String.valueOf(conBillingForm.getPayAmount())) && !RegexUtils.checkDecimals2(String.valueOf(conBillingForm.getPayAmount()))) {
            return ActionResult.fail("付款金额必须大于0，最多可以精确到小数点后两位");
        }
        ConBillingEntity entity = JsonUtil.getJsonToBean(conBillingForm, ConBillingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(conBillingForm.getStatus())) {
            conBillingService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        conBillingService.submit(entity.getId(), entity, conBillingForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改合同开票流程
     *
     * @param conBillingForm 表单对象
     * @param id             主键
     * @return
     */
    @ApiOperation("修改合同开票流程")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ConBillingForm conBillingForm, @PathVariable("id") String id) throws WorkFlowException {
        if (conBillingForm.getBillAmount() != null && !"".equals(conBillingForm.getBillAmount()) && !RegexUtils.checkDecimals2(String.valueOf(conBillingForm.getBillAmount()))) {
            return ActionResult.fail("开票金额必须大于0，最多可以精确到小数点后两位");
        }
        if (conBillingForm.getPayAmount() != null && !"".equals(conBillingForm.getPayAmount()) && !RegexUtils.checkDecimals2(String.valueOf(conBillingForm.getPayAmount()))) {
            return ActionResult.fail("付款金额必须大于0，最多可以精确到小数点后两位");
        }
        ConBillingEntity entity = JsonUtil.getJsonToBean(conBillingForm, ConBillingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(conBillingForm.getStatus())) {
            conBillingService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        conBillingService.submit(id, entity, conBillingForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
