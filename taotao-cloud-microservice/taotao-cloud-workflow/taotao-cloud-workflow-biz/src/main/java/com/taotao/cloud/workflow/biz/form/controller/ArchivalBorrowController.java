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
import jnpf.form.entity.ArchivalBorrowEntity;
import jnpf.form.model.archivalborrow.ArchivalBorrowForm;
import jnpf.form.model.archivalborrow.ArchivalBorrowInfoVO;
import jnpf.form.service.ArchivalBorrowService;
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
 * 档案借阅申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "档案借阅申请", value = "ArchivalBorrow")
@RestController
@RequestMapping("/api/workflow/Form/ArchivalBorrow")
public class ArchivalBorrowController {

    @Autowired
    private ArchivalBorrowService archivalBorrowService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取档案借阅申请信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取档案借阅申请信息")
    @GetMapping("/{id}")
    public ActionResult<ArchivalBorrowInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        ArchivalBorrowInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), ArchivalBorrowInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            ArchivalBorrowEntity entity = archivalBorrowService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, ArchivalBorrowInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建档案借阅申请
     *
     * @param archivalBorrowForm 表单对象
     * @return
     */
    @ApiOperation("新建档案借阅申请")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ArchivalBorrowForm archivalBorrowForm) throws WorkFlowException {
        if (archivalBorrowForm.getBorrowingDate() > archivalBorrowForm.getReturnDate()) {
            return ActionResult.fail("归还时间不能小于借阅时间");
        }
        ArchivalBorrowEntity entity = JsonUtil.getJsonToBean(archivalBorrowForm, ArchivalBorrowEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(archivalBorrowForm.getStatus())) {
            archivalBorrowService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        archivalBorrowService.submit(entity.getId(), entity, archivalBorrowForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改档案借阅申请
     *
     * @param archivalBorrowForm 表单对象
     * @param id                 主键
     * @return
     */
    @ApiOperation("修改档案借阅申请")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid ArchivalBorrowForm archivalBorrowForm, @PathVariable("id") String id) throws WorkFlowException {
        if (archivalBorrowForm.getBorrowingDate() > archivalBorrowForm.getReturnDate()) {
            return ActionResult.fail("归还时间不能小于借阅时间");
        }
        ArchivalBorrowEntity entity = JsonUtil.getJsonToBean(archivalBorrowForm, ArchivalBorrowEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(archivalBorrowForm.getStatus())) {
            archivalBorrowService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        archivalBorrowService.submit(id, entity, archivalBorrowForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
