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
import jnpf.form.entity.SalesSupportEntity;
import jnpf.form.model.salessupport.SalesSupportForm;
import jnpf.form.model.salessupport.SalesSupportInfoVO;
import jnpf.form.service.SalesSupportService;
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
 * 销售支持表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "销售支持表", value = "SalesSupport")
@RestController
@RequestMapping("/api/workflow/Form/SalesSupport")
public class SalesSupportController {

    @Autowired
    private SalesSupportService salesSupportService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取销售支持表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取销售支持表信息")
    @GetMapping("/{id}")
    public ActionResult<SalesSupportInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        SalesSupportInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), SalesSupportInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            SalesSupportEntity entity = salesSupportService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, SalesSupportInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建销售支持表
     *
     * @param salesSupportForm 表单对象
     * @return
     */
    @ApiOperation("新建保存销售支持表")
    @PostMapping
    public ActionResult create(@RequestBody SalesSupportForm salesSupportForm) throws WorkFlowException {
        SalesSupportEntity entity = JsonUtil.getJsonToBean(salesSupportForm, SalesSupportEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(salesSupportForm.getStatus())) {
            salesSupportService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        salesSupportService.submit(entity.getId(), entity,salesSupportForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改销售支持表
     *
     * @param salesSupportForm 表单对象
     * @param id               主键
     * @return
     */
    @ApiOperation("修改销售支持表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody SalesSupportForm salesSupportForm, @PathVariable("id") String id) throws WorkFlowException {
        SalesSupportEntity entity = JsonUtil.getJsonToBean(salesSupportForm, SalesSupportEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(salesSupportForm.getStatus())) {
            salesSupportService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        salesSupportService.submit(id, entity,salesSupportForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
