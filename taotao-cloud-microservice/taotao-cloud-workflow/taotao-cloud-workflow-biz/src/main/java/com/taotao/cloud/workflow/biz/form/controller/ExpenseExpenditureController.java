package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.ExpenseExpenditureEntity;
import jnpf.form.model.expenseexpenditure.ExpenseExpenditureForm;
import jnpf.form.model.expenseexpenditure.ExpenseExpenditureInfoVO;
import jnpf.form.service.ExpenseExpenditureService;
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
 * 费用支出单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "费用支出单", value = "ExpenseExpenditure")
@RestController
@RequestMapping("/api/workflow/Form/ExpenseExpenditure")
public class ExpenseExpenditureController {

    @Autowired
    private ExpenseExpenditureService expenseExpenditureService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取费用支出单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取费用支出单信息")
    @GetMapping("/{id}")
    public ActionResult<ExpenseExpenditureInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ExpenseExpenditureInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ExpenseExpenditureInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ExpenseExpenditureEntity entity = expenseExpenditureService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ExpenseExpenditureInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建费用支出单
     *
     * @param expenseExpenditureForm 表单对象
     * @return
     */
    @ApiOperation("新建费用支出单")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ExpenseExpenditureForm expenseExpenditureForm) throws WorkFlowException {
        ExpenseExpenditureEntity entity = JsonUtil.getJsonToBean(expenseExpenditureForm, ExpenseExpenditureEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(expenseExpenditureForm.getStatus())) {
            expenseExpenditureService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        expenseExpenditureService.submit(entity.getId(), entity, expenseExpenditureForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改费用支出单
     *
     * @param expenseExpenditureForm 表单对象
     * @param id                     主键
     * @return
     */
    @ApiOperation("修改费用支出单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ExpenseExpenditureForm expenseExpenditureForm, @PathVariable("id") String id) throws WorkFlowException {
        ExpenseExpenditureEntity entity = JsonUtil.getJsonToBean(expenseExpenditureForm, ExpenseExpenditureEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(expenseExpenditureForm.getStatus())) {
            expenseExpenditureService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        expenseExpenditureService.submit(id, entity, expenseExpenditureForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
