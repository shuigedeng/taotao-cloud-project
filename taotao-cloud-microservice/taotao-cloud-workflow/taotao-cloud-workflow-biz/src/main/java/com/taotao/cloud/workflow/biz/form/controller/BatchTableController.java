package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.base.util.RegexUtils;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.BatchTableEntity;
import jnpf.form.model.batchtable.BatchTableForm;
import jnpf.form.model.batchtable.BatchTableInfoVO;
import jnpf.form.service.BatchTableService;
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
 * 行文呈批表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "行文呈批表", value = "BatchTable")
@RestController
@RequestMapping("/api/workflow/Form/BatchTable")
public class BatchTableController {

    @Autowired
    private BatchTableService batchTableService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取行文呈批表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取行文呈批表信息")
    @GetMapping("/{id}")
    public ActionResult<BatchTableInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        BatchTableInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), BatchTableInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            BatchTableEntity entity = batchTableService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, BatchTableInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建行文呈批表
     *
     * @param batchTableForm 表单对象
     * @return
     */
    @ApiOperation("新建行文呈批表")
    @PostMapping
    public ActionResult create(@RequestBody @Valid BatchTableForm batchTableForm) throws WorkFlowException {
        if (batchTableForm.getShareNum() != null && StringUtil.isNotEmpty(batchTableForm.getShareNum()) && !RegexUtils.checkDigit2(batchTableForm.getShareNum())) {
            return ActionResult.fail("份数只能输入正整数");
        }
        BatchTableEntity entity = JsonUtil.getJsonToBean(batchTableForm, BatchTableEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(batchTableForm.getStatus())) {
            batchTableService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        batchTableService.submit(entity.getId(), entity, batchTableForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改行文呈批表
     *
     * @param batchTableForm 表单对象
     * @param id             主键
     * @return
     */
    @ApiOperation("修改行文呈批表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid BatchTableForm batchTableForm, @PathVariable("id") String id) throws WorkFlowException {
        if (batchTableForm.getShareNum() != null && StringUtil.isNotEmpty(batchTableForm.getShareNum()) && !RegexUtils.checkDigit2(batchTableForm.getShareNum())) {
            return ActionResult.fail("份数只能输入正整数");
        }
        BatchTableEntity entity = JsonUtil.getJsonToBean(batchTableForm, BatchTableEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(batchTableForm.getStatus())) {
            batchTableService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        batchTableService.submit(id, entity, batchTableForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
