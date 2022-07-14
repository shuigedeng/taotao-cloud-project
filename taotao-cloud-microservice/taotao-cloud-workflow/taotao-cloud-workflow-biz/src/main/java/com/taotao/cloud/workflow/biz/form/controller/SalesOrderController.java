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
import jnpf.form.entity.SalesOrderEntity;
import jnpf.form.entity.SalesOrderEntryEntity;
import jnpf.form.model.salesorder.SalesOrderEntryEntityInfoModel;
import jnpf.form.model.salesorder.SalesOrderForm;
import jnpf.form.model.salesorder.SalesOrderInfoVO;
import jnpf.form.service.SalesOrderService;
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
 * 销售订单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "销售订单", value = "SalesOrder")
@RestController
@RequestMapping("/api/workflow/Form/SalesOrder")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取销售订单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取销售订单信息")
    @GetMapping("/{id}")
    public ActionResult<SalesOrderInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        SalesOrderInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), SalesOrderInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            SalesOrderEntity entity = salesOrderService.getInfo(id);
            List<SalesOrderEntryEntity> entityList = salesOrderService.getSalesEntryList(id);
            vo = JsonUtil.getJsonToBean(entity, SalesOrderInfoVO.class);
            vo.setEntryList(JsonUtil.getJsonToList(entityList, SalesOrderEntryEntityInfoModel.class));
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建销售订单
     *
     * @param salesOrderForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("新建销售订单")
    @PostMapping
    public ActionResult create(@RequestBody SalesOrderForm salesOrderForm) throws WorkFlowException {
        SalesOrderEntity sales = JsonUtil.getJsonToBean(salesOrderForm, SalesOrderEntity.class);
        List<SalesOrderEntryEntity> salesEntryList = JsonUtil.getJsonToList(salesOrderForm.getEntryList(), SalesOrderEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(salesOrderForm.getStatus())) {
            salesOrderService.save(sales.getId(), sales, salesEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        salesOrderService.submit(sales.getId(), sales, salesEntryList,salesOrderForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改销售订单
     *
     * @param salesOrderForm 表单对象
     * @param id             主键
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("修改销售订单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody SalesOrderForm salesOrderForm, @PathVariable("id") String id) throws WorkFlowException {
        SalesOrderEntity sales = JsonUtil.getJsonToBean(salesOrderForm, SalesOrderEntity.class);
        List<SalesOrderEntryEntity> salesEntryList = JsonUtil.getJsonToList(salesOrderForm.getEntryList(), SalesOrderEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(salesOrderForm.getStatus())) {
            salesOrderService.save(id, sales, salesEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        salesOrderService.submit(id, sales, salesEntryList,salesOrderForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
