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
import jnpf.form.entity.BatchPackEntity;
import jnpf.form.model.batchpack.BatchPackForm;
import jnpf.form.model.batchpack.BatchPackInfoVO;
import jnpf.form.service.BatchPackService;
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
 * 批包装指令
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "批包装指令", value = "BatchPack")
@RestController
@RequestMapping("/api/workflow/Form/BatchPack")
public class BatchPackController {

    @Autowired
    private BatchPackService batchPackService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取批包装指令信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取批包装指令信息")
    @GetMapping("/{id}")
    public ActionResult<BatchPackInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        BatchPackInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), BatchPackInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            BatchPackEntity entity = batchPackService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, BatchPackInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建批包装指令
     *
     * @param batchPackForm 表单对象
     * @return
     */
    @ApiOperation("新建批包装指令")
    @PostMapping
    public ActionResult create(@RequestBody @Valid BatchPackForm batchPackForm) throws WorkFlowException {
        if (batchPackForm.getProductionQuty() != null && StringUtil.isNotEmpty(batchPackForm.getProductionQuty()) && !RegexUtils.checkDigit2(batchPackForm.getProductionQuty())) {
            return ActionResult.fail("批产数量只能输入正整数");
        }
        BatchPackEntity entity = JsonUtil.getJsonToBean(batchPackForm, BatchPackEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(batchPackForm.getStatus())) {
            batchPackService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        batchPackService.submit(entity.getId(), entity, batchPackForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改批包装指令
     *
     * @param batchPackForm 表单对象
     * @param id            主键
     * @return
     */
    @ApiOperation("修改批包装指令")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid BatchPackForm batchPackForm, @PathVariable("id") String id) throws WorkFlowException {
        if (batchPackForm.getProductionQuty() != null && StringUtil.isNotEmpty(batchPackForm.getProductionQuty()) && !RegexUtils.checkDigit2(batchPackForm.getProductionQuty())) {
            return ActionResult.fail("批产数量只能输入正整数");
        }
        BatchPackEntity entity = JsonUtil.getJsonToBean(batchPackForm, BatchPackEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(batchPackForm.getStatus())) {
            batchPackService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        batchPackService.submit(id, entity, batchPackForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
