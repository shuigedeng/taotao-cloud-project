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
import jnpf.form.entity.TravelApplyEntity;
import jnpf.form.model.travelapply.TravelApplyForm;
import jnpf.form.model.travelapply.TravelApplyInfoVO;
import jnpf.form.service.TravelApplyService;
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
 * 出差预支申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "出差预支申请单", value = "TravelApply")
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
    @ApiOperation("获取出差预支申请单信息")
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
    @ApiOperation("新建出差预支申请单")
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
    @ApiOperation("修改出差预支申请单")
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
