package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.DocumentApprovalEntity;
import jnpf.form.model.documentapproval.DocumentApprovalForm;
import jnpf.form.model.documentapproval.DocumentApprovalInfoVO;
import jnpf.form.service.DocumentApprovalService;
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
 * 文件签批意见表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "文件签批意见表", value = "DocumentApproval")
@RestController
@RequestMapping("/api/workflow/Form/DocumentApproval")
public class DocumentApprovalController {

    @Autowired
    private DocumentApprovalService documentApprovalService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取文件签批意见表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取文件签批意见表信息")
    @GetMapping("/{id}")
    public ActionResult<DocumentApprovalInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        DocumentApprovalInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), DocumentApprovalInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            DocumentApprovalEntity entity = documentApprovalService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, DocumentApprovalInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建文件签批意见表
     *
     * @param documentApprovalForm 表单对象
     * @return
     */
    @ApiOperation("新建文件签批意见表")
    @PostMapping
    public ActionResult create(@RequestBody @Valid DocumentApprovalForm documentApprovalForm) throws WorkFlowException {
        DocumentApprovalEntity entity = JsonUtil.getJsonToBean(documentApprovalForm, DocumentApprovalEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(documentApprovalForm.getStatus())) {
            documentApprovalService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        documentApprovalService.submit(entity.getId(), entity, documentApprovalForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改文件签批意见表
     *
     * @param documentApprovalForm 表单对象
     * @param id                   主键
     * @return
     */
    @ApiOperation("修改文件签批意见表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid DocumentApprovalForm documentApprovalForm, @PathVariable("id") String id) throws WorkFlowException {
        DocumentApprovalEntity entity = JsonUtil.getJsonToBean(documentApprovalForm, DocumentApprovalEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(documentApprovalForm.getStatus())) {
            documentApprovalService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        documentApprovalService.submit(id, entity, documentApprovalForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
