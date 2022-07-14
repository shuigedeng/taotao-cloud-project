package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.SalesSupportEntity;
import com.taotao.cloud.workflow.biz.form.model.salessupport.SalesSupportForm;
import com.taotao.cloud.workflow.biz.form.model.salessupport.SalesSupportInfoVO;
import com.taotao.cloud.workflow.biz.form.service.SalesSupportService;

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
 * 销售支持表
 */
@Tag(tags = "销售支持表", value = "SalesSupport")
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
    @Operation("获取销售支持表信息")
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
    @Operation("新建保存销售支持表")
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
    @Operation("修改销售支持表")
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
