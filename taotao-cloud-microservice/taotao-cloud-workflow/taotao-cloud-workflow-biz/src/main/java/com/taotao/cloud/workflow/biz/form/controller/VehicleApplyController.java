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
import jnpf.form.entity.VehicleApplyEntity;
import jnpf.form.model.vehicleapply.VehicleApplyForm;
import jnpf.form.model.vehicleapply.VehicleApplyInfoVO;
import jnpf.form.service.VehicleApplyService;
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
 * 车辆申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "车辆申请", value = "VehicleApply")
@RestController
@RequestMapping("/api/workflow/Form/0")
public class VehicleApplyController {

    @Autowired
    private VehicleApplyService vehicleApplyService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取车辆申请信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取车辆申请信息")
    @GetMapping("/{id}")
    public ActionResult<VehicleApplyInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        VehicleApplyInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), VehicleApplyInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            VehicleApplyEntity entity = vehicleApplyService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, VehicleApplyInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建车辆申请
     *
     * @param vehicleApplyForm 表单对象
     * @return
     */
    @ApiOperation("新建车辆申请")
    @PostMapping
    public ActionResult create(@RequestBody VehicleApplyForm vehicleApplyForm) throws WorkFlowException {
        VehicleApplyEntity entity = JsonUtil.getJsonToBean(vehicleApplyForm, VehicleApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(vehicleApplyForm.getStatus())) {
            vehicleApplyService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        vehicleApplyService.submit(entity.getId(), entity,vehicleApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 提交车辆申请
     *
     * @param vehicleApplyForm 表单对象
     * @param id               主键
     * @return
     */
    @ApiOperation("修改车辆申请")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody VehicleApplyForm vehicleApplyForm, @PathVariable("id") String id) throws WorkFlowException {
        VehicleApplyEntity entity = JsonUtil.getJsonToBean(vehicleApplyForm, VehicleApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(vehicleApplyForm.getStatus())) {
            vehicleApplyService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        vehicleApplyService.submit(id, entity,vehicleApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
