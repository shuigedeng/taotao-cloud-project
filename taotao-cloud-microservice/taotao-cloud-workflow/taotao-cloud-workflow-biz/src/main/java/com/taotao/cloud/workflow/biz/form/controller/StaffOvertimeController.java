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
import jnpf.form.entity.StaffOvertimeEntity;
import jnpf.form.model.staffovertime.StaffOvertimeForm;
import jnpf.form.model.staffovertime.StaffOvertimeInfoVO;
import jnpf.form.service.StaffOvertimeService;
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
 * 员工加班申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "员工加班申请表", value = "StaffOvertime")
@RestController
@RequestMapping("/api/workflow/Form/StaffOvertime")
public class StaffOvertimeController {

    @Autowired
    private StaffOvertimeService staffOvertimeService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取员工加班申请表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取员工加班申请表信息")
    @GetMapping("/{id}")
    public ActionResult<StaffOvertimeInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        StaffOvertimeInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), StaffOvertimeInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            StaffOvertimeEntity entity = staffOvertimeService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, StaffOvertimeInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建员工加班申请表
     *
     * @param staffOvertimeForm 表单对象
     * @return
     */
    @ApiOperation("新建员工加班申请表")
    @PostMapping
    public ActionResult create(@RequestBody StaffOvertimeForm staffOvertimeForm) throws WorkFlowException {
        if (staffOvertimeForm.getStartTime() > staffOvertimeForm.getEndTime()) {
            return ActionResult.fail("结束时间不能小于起始时间");
        }
        StaffOvertimeEntity entity = JsonUtil.getJsonToBean(staffOvertimeForm, StaffOvertimeEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(staffOvertimeForm.getStatus())) {
            staffOvertimeService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        staffOvertimeService.submit(entity.getId(), entity,staffOvertimeForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改
     *
     * @param staffOvertimeForm 表单对象
     * @param id                主键
     * @return
     */
    @ApiOperation("修改员工加班申请表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody StaffOvertimeForm staffOvertimeForm, @PathVariable("id") String id) throws WorkFlowException {
        if (staffOvertimeForm.getStartTime() > staffOvertimeForm.getEndTime()) {
            return ActionResult.fail("结束时间不能小于起始时间");
        }
        StaffOvertimeEntity entity = JsonUtil.getJsonToBean(staffOvertimeForm, StaffOvertimeEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(staffOvertimeForm.getStatus())) {
            staffOvertimeService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        staffOvertimeService.submit(id, entity,staffOvertimeForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
