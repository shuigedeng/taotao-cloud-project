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
import jnpf.form.entity.DebitBillEntity;
import jnpf.form.model.debitbill.DebitBillForm;
import jnpf.form.model.debitbill.DebitBillInfoVO;
import jnpf.form.service.DebitBillService;
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
 * 借支单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "借支单", value = "DebitBill")
@RestController
@RequestMapping("/api/workflow/Form/DebitBill")
public class DebitBillController {

    @Autowired
    private DebitBillService debitBillService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取借支单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取借支单信息")
    @GetMapping("/{id}")
    public ActionResult<DebitBillInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        DebitBillInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), DebitBillInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            DebitBillEntity entity = debitBillService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, DebitBillInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建借支单
     *
     * @param debitBillForm 表单对象
     * @return
     */
    @ApiOperation("新建借支单")
    @PostMapping
    public ActionResult create(@RequestBody @Valid DebitBillForm debitBillForm) throws WorkFlowException {
        if (debitBillForm.getAmountDebit() != null && !"".equals(String.valueOf(debitBillForm.getAmountDebit())) && !RegexUtils.checkDecimals2(String.valueOf(debitBillForm.getAmountDebit()))) {
            return ActionResult.fail("借支金额必须大于0，最多可以输入两位小数点");
        }
        DebitBillEntity entity = JsonUtil.getJsonToBean(debitBillForm, DebitBillEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(debitBillForm.getStatus())) {
            debitBillService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        debitBillService.submit(entity.getId(), entity, debitBillForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改借支单
     *
     * @param debitBillForm 表单对象
     * @param id            主键
     * @return
     */
    @ApiOperation("修改借支单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid DebitBillForm debitBillForm, @PathVariable("id") String id) throws WorkFlowException {
        if (debitBillForm.getAmountDebit() != null && !"".equals(String.valueOf(debitBillForm.getAmountDebit())) && !RegexUtils.checkDecimals2(String.valueOf(debitBillForm.getAmountDebit()))) {
            return ActionResult.fail("借支金额必须大于0，最多可以输入两位小数点");
        }
        DebitBillEntity entity = JsonUtil.getJsonToBean(debitBillForm, DebitBillEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(debitBillForm.getStatus())) {
            debitBillService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        debitBillService.submit(id, entity, debitBillForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
