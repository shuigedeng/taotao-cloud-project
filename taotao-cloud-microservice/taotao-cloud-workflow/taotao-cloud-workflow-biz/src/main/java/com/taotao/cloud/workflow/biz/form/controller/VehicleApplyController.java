package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.VehicleApplyEntity;
import com.taotao.cloud.workflow.biz.form.model.vehicleapply.VehicleApplyForm;
import com.taotao.cloud.workflow.biz.form.model.vehicleapply.VehicleApplyInfoVO;
import com.taotao.cloud.workflow.biz.form.service.VehicleApplyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.exception.DataException;
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
 */
@Tag(tags = "车辆申请", value = "VehicleApply")
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
    @Operation("获取车辆申请信息")
    @GetMapping("/{id}")
    public Result<VehicleApplyInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
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
        return Result.success(vo);
    }

    /**
     * 新建车辆申请
     *
     * @param vehicleApplyForm 表单对象
     * @return
     */
    @Operation("新建车辆申请")
    @PostMapping
    public Result create(@RequestBody VehicleApplyForm vehicleApplyForm) throws WorkFlowException {
        VehicleApplyEntity entity = JsonUtil.getJsonToBean(vehicleApplyForm, VehicleApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(vehicleApplyForm.getStatus())) {
            vehicleApplyService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        vehicleApplyService.submit(entity.getId(), entity,vehicleApplyForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 提交车辆申请
     *
     * @param vehicleApplyForm 表单对象
     * @param id               主键
     * @return
     */
    @Operation("修改车辆申请")
    @PutMapping("/{id}")
    public Result update(@RequestBody VehicleApplyForm vehicleApplyForm, @PathVariable("id") String id) throws WorkFlowException {
        VehicleApplyEntity entity = JsonUtil.getJsonToBean(vehicleApplyForm, VehicleApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(vehicleApplyForm.getStatus())) {
            vehicleApplyService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        vehicleApplyService.submit(id, entity,vehicleApplyForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
