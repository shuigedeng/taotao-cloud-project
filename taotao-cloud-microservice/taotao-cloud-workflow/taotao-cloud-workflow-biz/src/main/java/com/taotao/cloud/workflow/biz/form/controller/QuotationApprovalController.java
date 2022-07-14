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
import jnpf.form.entity.QuotationApprovalEntity;
import jnpf.form.model.quotationapproval.QuotationApprovalForm;
import jnpf.form.model.quotationapproval.QuotationApprovalInfoVO;
import jnpf.form.service.QuotationApprovalService;
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
 * 报价审批表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "报价审批表", value = "QuotationApproval")
@RestController
@RequestMapping("/api/workflow/Form/QuotationApproval")
public class QuotationApprovalController {


    @Autowired
    private QuotationApprovalService quotationApprovalService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取报价审批表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取报价审批表信息")
    @GetMapping("/{id}")
    public ActionResult<QuotationApprovalInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        QuotationApprovalInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), QuotationApprovalInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            QuotationApprovalEntity entity = quotationApprovalService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, QuotationApprovalInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建报价审批表
     *
     * @param quotationApprovalForm 表单对象
     * @return
     */
    @ApiOperation("新建报价审批表")
    @PostMapping
    public ActionResult create(@RequestBody QuotationApprovalForm quotationApprovalForm) throws WorkFlowException {
        QuotationApprovalEntity entity = JsonUtil.getJsonToBean(quotationApprovalForm, QuotationApprovalEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(quotationApprovalForm.getStatus())) {
            quotationApprovalService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        quotationApprovalService.submit(entity.getId(), entity,quotationApprovalForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改报价审批表
     *
     * @param quotationApprovalForm 表单对象
     * @param id                    主键
     * @return
     */
    @ApiOperation("修改报价审批表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody QuotationApprovalForm quotationApprovalForm, @PathVariable("id") String id) throws WorkFlowException {
        QuotationApprovalEntity entity = JsonUtil.getJsonToBean(quotationApprovalForm, QuotationApprovalEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(quotationApprovalForm.getStatus())) {
            quotationApprovalService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        quotationApprovalService.submit(id, entity,quotationApprovalForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
