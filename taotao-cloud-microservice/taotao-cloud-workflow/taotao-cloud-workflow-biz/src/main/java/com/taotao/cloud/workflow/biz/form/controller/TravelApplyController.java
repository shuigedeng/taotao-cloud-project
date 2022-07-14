package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.TravelApplyEntity;
import com.taotao.cloud.workflow.biz.form.model.travelapply.TravelApplyForm;
import com.taotao.cloud.workflow.biz.form.model.travelapply.TravelApplyInfoVO;
import com.taotao.cloud.workflow.biz.form.service.TravelApplyService;

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
 * 出差预支申请单
 */
@Tag(tags = "出差预支申请单", value = "TravelApply")
@RestController
@RequestMapping("/api/workflow/Form/TravelApply")
public class TravelApplyController {

    @Autowired
    private TravelApplyService travelApplyService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取出差预支申请单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取出差预支申请单信息")
    @GetMapping("/{id}")
    public ActionResult<TravelApplyInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        TravelApplyInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), TravelApplyInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            TravelApplyEntity entity = travelApplyService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, TravelApplyInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建出差预支申请单
     *
     * @param travelApplyForm 表单对象
     * @return
     */
    @Operation("新建出差预支申请单")
    @PostMapping
    public ActionResult create(@RequestBody TravelApplyForm travelApplyForm) throws WorkFlowException {
        TravelApplyEntity entity = JsonUtil.getJsonToBean(travelApplyForm, TravelApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(travelApplyForm.getStatus())) {
            travelApplyService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        travelApplyService.submit(entity.getId(), entity,travelApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改出差预支申请单
     *
     * @param travelApplyForm 表单对象
     * @param id              主键
     * @return
     */
    @Operation("修改出差预支申请单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody TravelApplyForm travelApplyForm, @PathVariable("id") String id) throws WorkFlowException {
        TravelApplyEntity entity = JsonUtil.getJsonToBean(travelApplyForm, TravelApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(travelApplyForm.getStatus())) {
            travelApplyService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        travelApplyService.submit(id, entity,travelApplyForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
