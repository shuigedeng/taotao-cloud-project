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
import jnpf.form.entity.OutgoingApplyEntity;
import jnpf.form.model.outgoingapply.OutgoingApplyForm;
import jnpf.form.model.outgoingapply.OutgoingApplyInfoVO;
import jnpf.form.service.OutgoingApplyService;
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
 * 外出申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "外出申请单", value = "OutgoingApply")
@RestController
@RequestMapping("/api/workflow/Form/OutgoingApply")
public class OutgoingApplyController {

    @Autowired
    private OutgoingApplyService outgoingApplyService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取外出申请单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取外出申请单信息")
    @GetMapping("/{id}")
    public ActionResult<OutgoingApplyInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        OutgoingApplyInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), OutgoingApplyInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            OutgoingApplyEntity entity = outgoingApplyService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, OutgoingApplyInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建外出申请单
     *
     * @param outgoingApplyForm 表单对象
     * @return
     */
    @ApiOperation("新建外出申请单")
    @PostMapping
    public ActionResult create(@RequestBody OutgoingApplyForm outgoingApplyForm) throws WorkFlowException {
        if (outgoingApplyForm.getStartTime() > outgoingApplyForm.getEndTime()) {
            return ActionResult.fail("结束时间不能小于起始时间");
        }
        OutgoingApplyEntity entity = JsonUtil.getJsonToBean(outgoingApplyForm, OutgoingApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(outgoingApplyForm.getStatus())) {
            outgoingApplyService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        outgoingApplyService.submit(entity.getId(), entity,outgoingApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改外出申请单
     *
     * @param outgoingApplyForm 表单对象
     * @param id                主键
     * @return
     */
    @ApiOperation("修改外出申请单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody OutgoingApplyForm outgoingApplyForm, @PathVariable("id") String id) throws WorkFlowException {
        if (outgoingApplyForm.getStartTime() > outgoingApplyForm.getEndTime()) {
            return ActionResult.fail("结束时间不能小于起始时间");
        }
        OutgoingApplyEntity entity = JsonUtil.getJsonToBean(outgoingApplyForm, OutgoingApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(outgoingApplyForm.getStatus())) {
            outgoingApplyService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        outgoingApplyService.submit(id, entity,outgoingApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
