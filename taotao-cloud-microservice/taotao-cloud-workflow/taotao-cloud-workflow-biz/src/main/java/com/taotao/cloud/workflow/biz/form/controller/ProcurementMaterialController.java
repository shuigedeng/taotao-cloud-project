package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.ProcurementEntryEntity;
import jnpf.form.entity.ProcurementMaterialEntity;
import jnpf.form.model.procurementmaterial.ProcurementEntryEntityInfoModel;
import jnpf.form.model.procurementmaterial.ProcurementMaterialForm;
import jnpf.form.model.procurementmaterial.ProcurementMaterialInfoVO;
import jnpf.form.service.ProcurementMaterialService;
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
 * 采购原材料
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "采购原材料", value = "ProcurementMaterial")
@RestController
@RequestMapping("/api/workflow/Form/ProcurementMaterial")
public class ProcurementMaterialController {

    @Autowired
    private ProcurementMaterialService procurementMaterialService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取采购原材料信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取采购原材料信息")
    @GetMapping("/{id}")
    public ActionResult<ProcurementMaterialInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ProcurementMaterialInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ProcurementMaterialInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ProcurementMaterialEntity entity = procurementMaterialService.getInfo(id);
            List<ProcurementEntryEntity> entityList = procurementMaterialService.getProcurementEntryList(id);
            vo = JsonUtil.getJsonToBean(entity, ProcurementMaterialInfoVO.class);
            vo.setEntryList(JsonUtil.getJsonToList(entityList, ProcurementEntryEntityInfoModel.class));
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建采购原材料
     *
     * @param procurementMaterialForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("新建采购原材料")
    @PostMapping
    public ActionResult create(@RequestBody ProcurementMaterialForm procurementMaterialForm) throws WorkFlowException {
        ProcurementMaterialEntity procurement = JsonUtil.getJsonToBean(procurementMaterialForm, ProcurementMaterialEntity.class);
        List<ProcurementEntryEntity> procurementEntryList = JsonUtil.getJsonToList(procurementMaterialForm.getEntryList(), ProcurementEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(procurementMaterialForm.getStatus())) {
            procurementMaterialService.save(procurement.getId(), procurement, procurementEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        procurementMaterialService.submit(procurement.getId(), procurement, procurementEntryList,procurementMaterialForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改采购原材料
     *
     * @param procurementMaterialForm 表单对象
     * @param id                      主键
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("修改采购原材料")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody ProcurementMaterialForm procurementMaterialForm, @PathVariable("id") String id) throws WorkFlowException {
        ProcurementMaterialEntity procurement = JsonUtil.getJsonToBean(procurementMaterialForm, ProcurementMaterialEntity.class);
        List<ProcurementEntryEntity> procurementEntryList = JsonUtil.getJsonToList(procurementMaterialForm.getEntryList(), ProcurementEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(procurementMaterialForm.getStatus())) {
            procurementMaterialService.save(id, procurement, procurementEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        procurementMaterialService.submit(id, procurement, procurementEntryList,procurementMaterialForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
