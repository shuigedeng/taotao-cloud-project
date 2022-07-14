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
import jnpf.form.entity.IncomeRecognitionEntity;
import jnpf.form.model.incomerecognition.IncomeRecognitionForm;
import jnpf.form.model.incomerecognition.IncomeRecognitionInfoVO;
import jnpf.form.service.IncomeRecognitionService;
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
 * 收入确认分析表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "收入确认分析表", value = "IncomeRecognition")
@RestController
@RequestMapping("/api/workflow/Form/IncomeRecognition")
public class IncomeRecognitionController {

    @Autowired
    private IncomeRecognitionService incomeRecognitionService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取收入确认分析表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取收入确认分析表信息")
    @GetMapping("/{id}")
    public ActionResult<IncomeRecognitionInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        IncomeRecognitionInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), IncomeRecognitionInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            IncomeRecognitionEntity entity = incomeRecognitionService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, IncomeRecognitionInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建收入确认分析表
     *
     * @param incomeRecognitionForm 表单对象
     * @return
     */
    @ApiOperation("新建收入确认分析表")
    @PostMapping
    public ActionResult create(@RequestBody @Valid IncomeRecognitionForm incomeRecognitionForm) throws WorkFlowException {
        IncomeRecognitionEntity entity = JsonUtil.getJsonToBean(incomeRecognitionForm, IncomeRecognitionEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(incomeRecognitionForm.getStatus())) {
            incomeRecognitionService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        incomeRecognitionService.submit(entity.getId(), entity, incomeRecognitionForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改收入确认分析表
     *
     * @param incomeRecognitionForm 表单对象
     * @param id                    主键
     * @return
     */
    @ApiOperation("修改收入确认分析表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid IncomeRecognitionForm incomeRecognitionForm, @PathVariable("id") String id) throws WorkFlowException {
        IncomeRecognitionEntity entity = JsonUtil.getJsonToBean(incomeRecognitionForm, IncomeRecognitionEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(incomeRecognitionForm.getStatus())) {
            incomeRecognitionService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        incomeRecognitionService.submit(id, entity, incomeRecognitionForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
