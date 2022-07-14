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
import jnpf.form.entity.OfficeSuppliesEntity;
import jnpf.form.model.officesupplies.OfficeSuppliesForm;
import jnpf.form.model.officesupplies.OfficeSuppliesInfoVO;
import jnpf.form.service.OfficeSuppliesService;
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
 * 领用办公用品申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "领用办公用品申请表", value = "OfficeSupplies")
@RestController
@RequestMapping("/api/workflow/Form/OfficeSupplies")
public class OfficeSuppliesController {

    @Autowired
    private OfficeSuppliesService officeSuppliesService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取领用办公用品申请表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取领用办公用品申请表信息")
    @GetMapping("/{id}")
    public ActionResult<OfficeSuppliesInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        OfficeSuppliesInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), OfficeSuppliesInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            OfficeSuppliesEntity entity = officeSuppliesService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, OfficeSuppliesInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建领用办公用品申请表
     *
     * @param officeSuppliesForm 表单对象
     * @return
     */
    @ApiOperation("新建领用办公用品申请表")
    @PostMapping
    public ActionResult create(@RequestBody OfficeSuppliesForm officeSuppliesForm) throws WorkFlowException {
        OfficeSuppliesEntity entity = JsonUtil.getJsonToBean(officeSuppliesForm, OfficeSuppliesEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(officeSuppliesForm.getStatus())) {
            officeSuppliesService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        officeSuppliesService.submit(entity.getId(), entity,officeSuppliesForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改领用办公用品申请表
     *
     * @param officeSuppliesForm 表单对象
     * @param id                 主键
     * @return
     */
    @ApiOperation("修改领用办公用品申请表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody OfficeSuppliesForm officeSuppliesForm, @PathVariable("id") String id) throws WorkFlowException {
        OfficeSuppliesEntity entity = JsonUtil.getJsonToBean(officeSuppliesForm, OfficeSuppliesEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(officeSuppliesForm.getStatus())) {
            officeSuppliesService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        officeSuppliesService.submit(id, entity,officeSuppliesForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
