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
import jnpf.form.entity.DocumentSigningEntity;
import jnpf.form.model.documentsigning.DocumentSigningForm;
import jnpf.form.model.documentsigning.DocumentSigningInfoVO;
import jnpf.form.service.DocumentSigningService;
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
 * 文件签阅表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "文件签阅表", value = "DocumentSigning")
@RestController
@RequestMapping("/api/workflow/Form/DocumentSigning")
public class DocumentSigningController {

    @Autowired
    private DocumentSigningService documentSigningService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取文件签阅表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取文件签阅表信息")
    @GetMapping("/{id}")
    public ActionResult<DocumentSigningInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        DocumentSigningInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), DocumentSigningInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            DocumentSigningEntity entity = documentSigningService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, DocumentSigningInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建文件签阅表
     *
     * @param documentSigningForm 表单对象
     * @return
     */
    @ApiOperation("新建文件签阅表")
    @PostMapping
    public ActionResult create(@RequestBody @Valid DocumentSigningForm documentSigningForm) throws WorkFlowException {
        DocumentSigningEntity entity = JsonUtil.getJsonToBean(documentSigningForm, DocumentSigningEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(documentSigningForm.getStatus())) {
            documentSigningService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        documentSigningService.submit(entity.getId(), entity, documentSigningForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改文件签阅表
     *
     * @param documentSigningForm 表单对象
     * @param id                  主键
     * @return
     */
    @ApiOperation("修改文件签阅表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid DocumentSigningForm documentSigningForm, @PathVariable("id") String id) throws WorkFlowException {
        DocumentSigningEntity entity = JsonUtil.getJsonToBean(documentSigningForm, DocumentSigningEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(documentSigningForm.getStatus())) {
            documentSigningService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        documentSigningService.submit(id, entity, documentSigningForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
