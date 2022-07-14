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
import jnpf.form.entity.ReceiptSignEntity;
import jnpf.form.model.receiptsign.ReceiptSignForm;
import jnpf.form.model.receiptsign.ReceiptSignInfoVO;
import jnpf.form.service.ReceiptSignService;
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
 * 收文签呈单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "收文签呈单", value = "ReceiptSign")
@RestController
@RequestMapping("/api/workflow/Form/ReceiptSign")
public class ReceiptSignController {

    @Autowired
    private ReceiptSignService receiptSignService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取收文签呈单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取收文签呈单信息")
    @GetMapping("/{id}")
    public ActionResult<ReceiptSignInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ReceiptSignInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ReceiptSignInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ReceiptSignEntity entity = receiptSignService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ReceiptSignInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建收文签呈单
     *
     * @param receiptSignForm 表单对象
     * @return
     */
    @ApiOperation("新建收文签呈单")
    @PostMapping
    public ActionResult create(@RequestBody ReceiptSignForm receiptSignForm) throws WorkFlowException {
        ReceiptSignEntity entity = JsonUtil.getJsonToBean(receiptSignForm, ReceiptSignEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(receiptSignForm.getStatus())) {
            receiptSignService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        receiptSignService.submit(entity.getId(), entity,receiptSignForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改收文签呈单
     *
     * @param receiptSignForm 表单对象
     * @param id              主键
     * @return
     */
    @ApiOperation("修改收文签呈单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody ReceiptSignForm receiptSignForm, @PathVariable("id") String id) throws WorkFlowException {
        ReceiptSignEntity entity = JsonUtil.getJsonToBean(receiptSignForm, ReceiptSignEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(receiptSignForm.getStatus())) {
            receiptSignService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        receiptSignService.submit(id, entity,receiptSignForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
