package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.MaterialEntryEntity;
import jnpf.form.entity.MaterialRequisitionEntity;
import jnpf.form.model.materialrequisition.MaterialEntryEntityInfoModel;
import jnpf.form.model.materialrequisition.MaterialRequisitionForm;
import jnpf.form.model.materialrequisition.MaterialRequisitionInfoVO;
import jnpf.form.service.MaterialRequisitionService;
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
 * 领料单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "领料单", value = "MaterialRequisition")
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
    @ApiOperation("获取领料单信息")
    @GetMapping("/{id}")
    public ActionResult<MaterialRequisitionInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
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
        return ActionResult.success(vo);
    }

    /**
     * 新建领料单
     *
     * @param materialRequisitionForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("新建领料单")
    @PostMapping
    public ActionResult create(@RequestBody @Valid MaterialRequisitionForm materialRequisitionForm) throws WorkFlowException {
        MaterialRequisitionEntity material = JsonUtil.getJsonToBean(materialRequisitionForm, MaterialRequisitionEntity.class);
        List<MaterialEntryEntity> materialEntryList = JsonUtil.getJsonToList(materialRequisitionForm.getEntryList(), MaterialEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(materialRequisitionForm.getStatus())) {
            materialRequisitionService.save(material.getId(), material, materialEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        materialRequisitionService.submit(material.getId(), material, materialEntryList,materialRequisitionForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改领料单
     *
     * @param materialRequisitionForm 表单对象
     * @param id                      主键
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("修改领料单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid MaterialRequisitionForm materialRequisitionForm, @PathVariable("id") String id) throws WorkFlowException {
        MaterialRequisitionEntity material = JsonUtil.getJsonToBean(materialRequisitionForm, MaterialRequisitionEntity.class);
        List<MaterialEntryEntity> materialEntryList = JsonUtil.getJsonToList(materialRequisitionForm.getEntryList(), MaterialEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(materialRequisitionForm.getStatus())) {
            materialRequisitionService.save(id, material, materialEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        materialRequisitionService.submit(id, material, materialEntryList,materialRequisitionForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
