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
import jnpf.form.entity.ViolationHandlingEntity;
import jnpf.form.model.violationhandling.ViolationHandlingForm;
import jnpf.form.model.violationhandling.ViolationHandlingInfoVO;
import jnpf.form.service.ViolationHandlingService;
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
 * 违章处理申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "违章处理申请表", value = "ViolationHandling")
@RestController
@RequestMapping("/api/workflow/Form/ViolationHandling")
public class ViolationHandlingController {

    @Autowired
    private ViolationHandlingService violationHandlingService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取违章处理申请表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取违章处理申请表信息")
    @GetMapping("/{id}")
    public ActionResult<ViolationHandlingInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ViolationHandlingInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ViolationHandlingInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ViolationHandlingEntity entity = violationHandlingService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ViolationHandlingInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建违章处理申请表
     *
     * @param violationHandlingForm 表单对象
     * @return
     */
    @ApiOperation("新建违章处理申请表")
    @PostMapping
    public ActionResult create(@RequestBody ViolationHandlingForm violationHandlingForm) throws WorkFlowException {
        ViolationHandlingEntity entity = JsonUtil.getJsonToBean(violationHandlingForm, ViolationHandlingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(violationHandlingForm.getStatus())) {
            violationHandlingService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        violationHandlingService.submit(entity.getId(), entity,violationHandlingForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改违章处理申请表
     *
     * @param violationHandlingForm 表单对象
     * @param id                    主键
     * @return
     */
    @ApiOperation("修改违章处理申请表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody ViolationHandlingForm violationHandlingForm, @PathVariable("id") String id) throws WorkFlowException {
        ViolationHandlingEntity entity = JsonUtil.getJsonToBean(violationHandlingForm, ViolationHandlingEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(violationHandlingForm.getStatus())) {
            violationHandlingService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        violationHandlingService.submit(id, entity,violationHandlingForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
