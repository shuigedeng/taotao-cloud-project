package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.TravelReimbursementEntity;
import com.taotao.cloud.workflow.biz.form.model.travelreimbursement.TravelReimbursementForm;
import com.taotao.cloud.workflow.biz.form.model.travelreimbursement.TravelReimbursementInfoVO;
import com.taotao.cloud.workflow.biz.form.service.TravelReimbursementService;

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
 * 差旅报销申请表
 *
 */
@Tag(tags = "差旅报销申请表", value = "TravelReimbursement")
@RestController
@RequestMapping("/api/workflow/Form/TravelReimbursement")
public class TravelReimbursementController {

    @Autowired
    private TravelReimbursementService travelReimbursementService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取差旅报销申请表信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取差旅报销申请表信息")
    @GetMapping("/{id}")
    public Result<TravelReimbursementInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        TravelReimbursementInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtils.getJsonToBean(operator.getDraftData(), TravelReimbursementInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            TravelReimbursementEntity entity = travelReimbursementService.getInfo(id);
            vo = JsonUtils.getJsonToBean(entity, TravelReimbursementInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建差旅报销申请表
     *
     * @param travelReimbursementForm 表单对象
     * @return
     */
    @Operation("新建差旅报销申请表")
    @PostMapping
    public Result create(@RequestBody TravelReimbursementForm travelReimbursementForm) throws WorkFlowException {
        if (travelReimbursementForm.getSetOutDate() > travelReimbursementForm.getReturnDate()) {
            return Result.fail("结束时间不能小于起始时间");
        }
        TravelReimbursementEntity entity = JsonUtils.getJsonToBean(travelReimbursementForm, TravelReimbursementEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(travelReimbursementForm.getStatus())) {
            travelReimbursementService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        travelReimbursementService.submit(entity.getId(), entity,travelReimbursementForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改差旅报销申请表
     *
     * @param travelReimbursementForm 表单对象
     * @param id                      主键
     * @return
     */
    @Operation("修改差旅报销申请表")
    @PutMapping("/{id}")
    public Result update(@RequestBody TravelReimbursementForm travelReimbursementForm, @PathVariable("id") String id) throws WorkFlowException {
        if (travelReimbursementForm.getSetOutDate() > travelReimbursementForm.getReturnDate()) {
            return Result.fail("结束时间不能小于起始时间");
        }
        TravelReimbursementEntity entity = JsonUtils.getJsonToBean(travelReimbursementForm, TravelReimbursementEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(travelReimbursementForm.getStatus())) {
            travelReimbursementService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        travelReimbursementService.submit(id, entity,travelReimbursementForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
