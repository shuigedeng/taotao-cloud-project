package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.WorkContactSheetEntity;
import com.taotao.cloud.workflow.biz.form.model.workcontactsheet.WorkContactSheetForm;
import com.taotao.cloud.workflow.biz.form.model.workcontactsheet.WorkContactSheetInfoVO;
import com.taotao.cloud.workflow.biz.form.service.WorkContactSheetService;

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
 * 工作联系单
 *
 */
@Tag(tags = "工作联系单", value = "WorkContactSheet")
@RestController
@RequestMapping("/api/workflow/Form/WorkContactSheet")
public class WorkContactSheetController {

    @Autowired
    private WorkContactSheetService workContactSheetService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取工作联系单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取工作联系单信息")
    @GetMapping("/{id}")
    public ActionResult<WorkContactSheetInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        WorkContactSheetInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), WorkContactSheetInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            WorkContactSheetEntity entity = workContactSheetService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, WorkContactSheetInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建工作联系单
     *
     * @param workContactSheetForm 表单对象
     * @return
     */
    @Operation("新建工作联系单")
    @PostMapping
    public ActionResult create(@RequestBody WorkContactSheetForm workContactSheetForm) throws WorkFlowException {
        WorkContactSheetEntity entity = JsonUtil.getJsonToBean(workContactSheetForm, WorkContactSheetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(workContactSheetForm.getStatus())) {
            workContactSheetService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        workContactSheetService.submit(entity.getId(), entity,workContactSheetForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改工作联系单
     *
     * @param workContactSheetForm 表单对象
     * @param id                   主键
     * @return
     */
    @Operation("修改工作联系单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody WorkContactSheetForm workContactSheetForm, @PathVariable("id") String id) throws WorkFlowException {
        WorkContactSheetEntity entity = JsonUtil.getJsonToBean(workContactSheetForm, WorkContactSheetEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(workContactSheetForm.getStatus())) {
            workContactSheetService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        workContactSheetService.submit(id, entity,workContactSheetForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
