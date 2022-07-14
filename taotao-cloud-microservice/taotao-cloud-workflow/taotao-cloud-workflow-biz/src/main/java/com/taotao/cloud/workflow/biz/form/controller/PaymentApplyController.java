package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.PaymentApplyEntity;
import jnpf.form.model.paymentapply.PaymentApplyForm;
import jnpf.form.model.paymentapply.PaymentApplyInfoVO;
import jnpf.form.service.PaymentApplyService;
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
 * 付款申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "付款申请单", value = "PaymentApply")
@RestController
@RequestMapping("/api/workflow/Form/PaymentApply")
public class PaymentApplyController {

    @Autowired
    private PaymentApplyService paymentApplyService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取付款申请单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取付款申请单信息")
    @GetMapping("/{id}")
    public ActionResult<PaymentApplyInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        PaymentApplyInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), PaymentApplyInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            PaymentApplyEntity entity = paymentApplyService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, PaymentApplyInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建付款申请单
     *
     * @param paymentApplyForm 表单对象
     * @return
     */
    @ApiOperation("新建付款申请单")
    @PostMapping
    public ActionResult create(@RequestBody PaymentApplyForm paymentApplyForm) throws WorkFlowException {
        PaymentApplyEntity entity = JsonUtil.getJsonToBean(paymentApplyForm, PaymentApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(paymentApplyForm.getStatus())) {
            paymentApplyService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        paymentApplyService.submit(entity.getId(), entity,paymentApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改付款申请单
     *
     * @param paymentApplyForm 表单对象
     * @param id               主键
     * @return
     */
    @ApiOperation("修改付款申请单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody PaymentApplyForm paymentApplyForm, @PathVariable("id") String id) throws WorkFlowException {
        PaymentApplyEntity entity = JsonUtil.getJsonToBean(paymentApplyForm, PaymentApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(paymentApplyForm.getStatus())) {
            paymentApplyService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        paymentApplyService.submit(id, entity,paymentApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
