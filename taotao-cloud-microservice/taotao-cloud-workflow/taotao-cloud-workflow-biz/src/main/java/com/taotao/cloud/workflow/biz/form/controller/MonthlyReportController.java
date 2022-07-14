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
import jnpf.form.entity.MonthlyReportEntity;
import jnpf.form.model.monthlyreport.MonthlyReportForm;
import jnpf.form.model.monthlyreport.MonthlyReportInfoVO;
import jnpf.form.service.MonthlyReportService;
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
 * 月工作总结
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "月工作总结", value = "MonthlyReport")
@RestController
@RequestMapping("/api/workflow/Form/MonthlyReport")
public class MonthlyReportController {

    @Autowired
    private MonthlyReportService monthlyReportService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取月工作总结信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取月工作总结信息")
    @GetMapping("/{id}")
    public ActionResult<MonthlyReportInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        MonthlyReportInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), MonthlyReportInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            MonthlyReportEntity entity = monthlyReportService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, MonthlyReportInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建月工作总结
     *
     * @param monthlyReportForm 表单对象
     * @return
     */
    @ApiOperation("新建月工作总结")
    @PostMapping
    public ActionResult create(@RequestBody MonthlyReportForm monthlyReportForm) throws WorkFlowException {
        MonthlyReportEntity entity = JsonUtil.getJsonToBean(monthlyReportForm, MonthlyReportEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(monthlyReportForm.getStatus())) {
            monthlyReportService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        monthlyReportService.submit(entity.getId(), entity,monthlyReportForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改月工作总结
     *
     * @param monthlyReportForm 表单对象
     * @param id                主键
     * @return
     */
    @ApiOperation("修改月工作总结")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody MonthlyReportForm monthlyReportForm, @PathVariable("id") String id) throws WorkFlowException {
        MonthlyReportEntity entity = JsonUtil.getJsonToBean(monthlyReportForm, MonthlyReportEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(monthlyReportForm.getStatus())) {
            monthlyReportService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        monthlyReportService.submit(id, entity,monthlyReportForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
