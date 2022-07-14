package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.MaterialEntryEntity;
import com.taotao.cloud.workflow.biz.form.entity.MaterialRequisitionEntity;
import com.taotao.cloud.workflow.biz.form.model.materialrequisition.MaterialEntryEntityInfoModel;
import com.taotao.cloud.workflow.biz.form.model.materialrequisition.MaterialRequisitionForm;
import com.taotao.cloud.workflow.biz.form.model.materialrequisition.MaterialRequisitionInfoVO;
import com.taotao.cloud.workflow.biz.form.service.MaterialRequisitionService;

import java.util.List;
import javax.validation.Valid;

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
 * 领料单
 */
@Tag(tags = "领料单", value = "MaterialRequisition")
@RestController
@RequestMapping("/api/workflow/Form/MaterialRequisition")
public class MaterialRequisitionController {

    @Autowired
    private MaterialRequisitionService materialRequisitionService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取领料单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取领料单信息")
    @GetMapping("/{id}")
    public Result<MaterialRequisitionInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        MaterialRequisitionInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), MaterialRequisitionInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            MaterialRequisitionEntity entity = materialRequisitionService.getInfo(id);
            List<MaterialEntryEntity> entityList = materialRequisitionService.getMaterialEntryList(id);
            vo = JsonUtil.getJsonToBean(entity, MaterialRequisitionInfoVO.class);
            vo.setEntryList(JsonUtil.getJsonToList(entityList, MaterialEntryEntityInfoModel.class));
        }
        return Result.success(vo);
    }

    /**
     * 新建领料单
     *
     * @param materialRequisitionForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @Operation("新建领料单")
    @PostMapping
    public Result create(@RequestBody @Valid MaterialRequisitionForm materialRequisitionForm) throws WorkFlowException {
        MaterialRequisitionEntity material = JsonUtil.getJsonToBean(materialRequisitionForm, MaterialRequisitionEntity.class);
        List<MaterialEntryEntity> materialEntryList = JsonUtil.getJsonToList(materialRequisitionForm.getEntryList(), MaterialEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(materialRequisitionForm.getStatus())) {
            materialRequisitionService.save(material.getId(), material, materialEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        materialRequisitionService.submit(material.getId(), material, materialEntryList,materialRequisitionForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改领料单
     *
     * @param materialRequisitionForm 表单对象
     * @param id                      主键
     * @return
     * @throws WorkFlowException
     */
    @Operation("修改领料单")
    @PutMapping("/{id}")
    public Result update(@RequestBody @Valid MaterialRequisitionForm materialRequisitionForm, @PathVariable("id") String id) throws WorkFlowException {
        MaterialRequisitionEntity material = JsonUtil.getJsonToBean(materialRequisitionForm, MaterialRequisitionEntity.class);
        List<MaterialEntryEntity> materialEntryList = JsonUtil.getJsonToList(materialRequisitionForm.getEntryList(), MaterialEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(materialRequisitionForm.getStatus())) {
            materialRequisitionService.save(id, material, materialEntryList);
            return Result.success(MsgCode.SU002.get());
        }
        materialRequisitionService.submit(id, material, materialEntryList,materialRequisitionForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
