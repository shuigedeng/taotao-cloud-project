package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.ArchivalBorrowEntity;
import com.taotao.cloud.workflow.biz.form.model.archivalborrow.ArchivalBorrowForm;
import com.taotao.cloud.workflow.biz.form.model.archivalborrow.ArchivalBorrowInfoVO;
import com.taotao.cloud.workflow.biz.form.service.ArchivalBorrowService;

import javax.validation.Valid;

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
 * 档案借阅申请
 */
@Tag(tags = "档案借阅申请", value = "ArchivalBorrow")
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
    @Operation("获取档案借阅申请信息")
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
    @Operation("新建档案借阅申请")
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
    @Operation("修改档案借阅申请")
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
