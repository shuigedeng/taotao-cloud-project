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
import jnpf.form.entity.LeaveApplyEntity;
import jnpf.form.model.leaveapply.LeaveApplyForm;
import jnpf.form.model.leaveapply.LeaveApplyInfoVO;
import jnpf.form.service.LeaveApplyService;
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
 * 请假申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "请假申请", value = "LeaveApply")
@RestController
@RequestMapping("/api/workflow/Form/LeaveApply")
public class LeaveApplyController {

    @Autowired
    private LeaveApplyService leaveApplyService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取请假申请信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取请假申请信息")
    @GetMapping("/{id}")
    public ActionResult<LeaveApplyInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        LeaveApplyInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), LeaveApplyInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            LeaveApplyEntity entity = leaveApplyService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, LeaveApplyInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建请假申请
     *
     * @param leaveApplyForm 表单对象
     * @return
     */
    @ApiOperation("新建请假申请")
    @PostMapping
    public ActionResult create(@RequestBody @Valid LeaveApplyForm leaveApplyForm) throws WorkFlowException {
        if (leaveApplyForm.getLeaveStartTime() > leaveApplyForm.getLeaveEndTime()) {
            return ActionResult.fail("结束时间不能小于起始时间");
        }
        if (!RegexUtils.checkLeave(leaveApplyForm.getLeaveDayCount())) {
            return ActionResult.fail("请假天数只能是0.5的倍数");
        }
        if (!RegexUtils.checkLeave(leaveApplyForm.getLeaveHour())) {
            return ActionResult.fail("请假小时只能是0.5的倍数");
        }
        LeaveApplyEntity entity = JsonUtil.getJsonToBean(leaveApplyForm, LeaveApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(leaveApplyForm.getStatus())) {
            leaveApplyService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        leaveApplyService.submit(entity.getId(), entity, leaveApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改请假申请
     *
     * @param leaveApplyForm 表单对象
     * @param id             主键
     * @return
     */
    @ApiOperation("修改请假申请")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid LeaveApplyForm leaveApplyForm, @PathVariable("id") String id) throws WorkFlowException {
        if (leaveApplyForm.getLeaveStartTime() > leaveApplyForm.getLeaveEndTime()) {
            return ActionResult.fail("结束时间不能小于起始时间");
        }
        if (!RegexUtils.checkLeave(leaveApplyForm.getLeaveDayCount())) {
            return ActionResult.fail("请假天数只能是0.5的倍数");
        }
        if (!RegexUtils.checkLeave(leaveApplyForm.getLeaveHour())) {
            return ActionResult.fail("请假小时只能是0.5的倍数");
        }
        LeaveApplyEntity entity = JsonUtil.getJsonToBean(leaveApplyForm, LeaveApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(leaveApplyForm.getStatus())) {
            leaveApplyService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        leaveApplyService.submit(id, entity, leaveApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
